package com.example.wangqiang.kotlin.h264

import android.media.MediaCodec
import android.media.MediaFormat
import android.os.Build
import android.util.Log
import android.view.Surface
import androidx.annotation.RequiresApi
import com.example.wangqiang.kotlin.h264.util.SpsDecoderUtil
import com.example.wangqiang.kotlin.h264.util.Util
import java.io.*

/**
 * H264播放器
 */
class H264Player(private var path: String, private var surface: Surface) : Runnable {
//    private val spsByteArray: ByteArray = Util.hexStringToByteArray("00 00 00 01 67 42 00 0A 8D 8D 40 28 02 AD 35 05 02 02 07 84 42 29 C0")
    private lateinit var mediaCodeC: MediaCodec

    init {
        initPlayer()
    }

    private fun initPlayer() {
//        val spsDecoderUtil=SpsDecoderUtil(spsByteArray)
//        spsDecoderUtil.startDecode()
        mediaCodeC =
            MediaCodec.createDecoderByType(MediaFormat.MIMETYPE_VIDEO_AVC)//创建解码器,如果抛出异常，说明dps芯片不支持，才用软解方式，FFmpeg
        //设置视频的参数信息，dps芯片并不知道
        val mediaFormat = MediaFormat.createVideoFormat("video/avc", 368, 384)
        mediaFormat.setInteger(MediaFormat.KEY_FRAME_RATE, 15)//帧率
        mediaCodeC.configure(mediaFormat, surface, null, 0)//配置参数,crypto:加密
    }

    fun play() {
        mediaCodeC.start()
        Thread(this).start()
    }

    override fun run() {
        val bytes = getBytes(path)
        val byteBuffers = mediaCodeC.inputBuffers //dps芯片会将能用的bytebuff队列返回，所有app都可以获取到。
        var frameStartIndex: Int = 0
        bytes?.let {
            while (true) {
                val availabIndex = mediaCodeC.dequeueInputBuffer(10000)//查找时间内，可以用的bytebuffer，返回索引
                if (availabIndex >= 0) {
                    val byteBuffer = byteBuffers.get(availabIndex)
                    val nextFrameStart = getFrame(it, frameStartIndex + 2)
                    byteBuffer.clear() //将原有的数据清空
                    //将需要解码的一帧数据，byte放入拿到的bytebuffer
                    byteBuffer.put(bytes, frameStartIndex, nextFrameStart - frameStartIndex)
                    //通知dsp芯片，对index的bytebuffer解码
                    mediaCodeC.queueInputBuffer(
                        availabIndex,
                        0,
                        nextFrameStart - frameStartIndex,
                        0,
                        0
                    )
                    frameStartIndex = nextFrameStart
                } else {
                    continue
                }
                val info = MediaCodec.BufferInfo()
                //硬件会做一一匹配，这个时候取到的outbuffer一定是之前需要解码的
                val outPutIndex = mediaCodeC.dequeueOutputBuffer(info, 10000)
                if (outPutIndex > 0) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        //释放outbuffer，render：true 如果有surface，会自动解码的数据加载到suface中，false：没有surface
                        mediaCodeC.releaseOutputBuffer(outPutIndex, true)
                    }
                }
            }
        }
    }

    /**
     * 直接读取视频字节数，小视频
     * 如果视频过大，需要分块读取
     */
    @Throws(IOException::class)
    fun getBytes(path: String?): ByteArray? {
        val `is`: InputStream = DataInputStream(FileInputStream(File(path)))
        var len: Int = 0
        val size = 1024
        var buf: ByteArray
        val bos = ByteArrayOutputStream()
        buf = ByteArray(size)
        while (`is`.read(buf, 0, size).also { len = it } != -1) bos.write(buf, 0, len)
        buf = bos.toByteArray()
        return buf
    }

    fun getFrame(bytes: ByteArray, start: Int): Int {
        for (index in start..bytes.size) {
            if ((bytes[index].toInt() == 0x00 && bytes[index + 1].toInt() == 0x00
                        && bytes[index + 2].toInt() == 0x00 && bytes[index + 3].toInt() == 0x01)
                || (bytes[index].toInt() == 0x00 && bytes[index + 1].toInt() == 0x00
                        && bytes[index + 2].toInt() == 0x01)
            ) {
                return index
            }
        }
        return -1
    }

    fun ondestory() {

    }
}
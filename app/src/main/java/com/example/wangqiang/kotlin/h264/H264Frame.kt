package com.example.wangqiang.kotlin.h264

import android.graphics.*
import android.media.MediaCodec
import android.media.MediaFormat
import android.os.Build
import android.widget.ImageView
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import java.io.*

/**
 * author : wangqiang
 * e-mail : wangqiang@geely.com
 * time   : 2021/01/06
 * desc   : mediacodec获取帧图片
 * version: 1.0
 */
class H264Frame(private var path: String, private var imageView: ImageView) {
    private lateinit var mediaCodeC: MediaCodec
    private var disposable: Disposable? = null
    private var frameInt = 0

    init {
        initPlayer()
    }

    companion object {
        private const val TIME: Long = 10000
    }

    private fun initPlayer() {
        mediaCodeC =
            MediaCodec.createDecoderByType("video/avc")//创建解码器,如果抛出异常，说明dps芯片不支持，才用软解方式，FFmpeg
        //设置视频的参数信息，dps芯片并不知道
        val mediaFormat = MediaFormat.createVideoFormat("video/avc", 368, 384)
        mediaFormat.setInteger(MediaFormat.KEY_FRAME_RATE, 15)//帧率
        mediaCodeC.configure(mediaFormat, null, null, 0)//配置参数,crypto:加密
    }

    fun play() {
        mediaCodeC.start()
        run()
    }

    fun run() {
        disposable = Observable.create<Bitmap> { emitter ->
            val bytes = getBytes(path)
            val byteBuffers = mediaCodeC.inputBuffers
            var startIndex: Int = 0
            bytes?.let {
                while (true) {
                    var availabIndex = mediaCodeC.dequeueInputBuffer(TIME)
                    if (availabIndex > 0) {
                        var nextIndex = getFrame(it, startIndex + 2)
                        var byteBuffer = byteBuffers.get(availabIndex)
                        byteBuffer.clear()
                        byteBuffer.put(bytes, startIndex, nextIndex - startIndex)
                        mediaCodeC.queueInputBuffer(availabIndex, 0, nextIndex - startIndex, 0, 0)
                        startIndex = nextIndex

                    } else {
                        continue
                    }
                    val bufferInfo = MediaCodec.BufferInfo()
                    val outIndex = mediaCodeC.dequeueOutputBuffer(bufferInfo, TIME)
                    if (outIndex > 0) {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                            //bytebuff属于dsp芯片返回，无法直接使用，需要放到其他容器，不能对bytebuffer直接操作
                            val outPutBuffer = mediaCodeC.getOutputBuffer(outIndex)
                            outPutBuffer?.let {
                                //outPutBuffer.remaining() 获取bytebuffer的真实长度
                                var byteArray = ByteArray(outPutBuffer.remaining())
                                //将bytebuffer数据放入到字节数组中
                                it.get(byteArray)
                                //将得到的字节数组生成yuv
                                val yuvImage = YuvImage(byteArray, ImageFormat.NV21, 368, 384, null)
                                val byteArrayOutputStream = ByteArrayOutputStream()
                                //将yuv图片，转换成字节流
                                yuvImage.compressToJpeg(
                                    Rect(0, 0, 368, 384),
                                    100,
                                    byteArrayOutputStream
                                )
                                //将yuv转换后的字节流在生成rgb的字节流
                                val bitmapByteArray = byteArrayOutputStream.toByteArray()
                                val bitmap = BitmapFactory.decodeByteArray(
                                    bitmapByteArray,
                                    0,
                                    bitmapByteArray.size
                                )
                                //每隔20帧展示图片
                                if (frameInt % 20 == 0) {
                                    emitter.onNext(bitmap)
                                }
                                frameInt++
                            }
                        }
                        mediaCodeC.releaseOutputBuffer(outIndex, false)
                    }
                }
            }
        }.subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { bitmap ->
                bitmap?.let {
                    imageView.setImageBitmap(bitmap)
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
        disposable?.dispose()
    }
}
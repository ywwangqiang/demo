package com.example.h265toupinsend

import android.hardware.display.DisplayManager
import android.media.MediaCodec
import android.media.MediaCodecInfo
import android.media.MediaFormat
import android.media.projection.MediaProjection
import android.os.Build
import java.nio.ByteBuffer
import kotlin.experimental.and

/**
 *     author : wangqiang
 *     e-mail : wangqiang@geely.com
 *     time   : 2021/01/26
 *     desc   : h265投屏发送端
 *     version: 1.0
 */
class H265Code constructor(
    private val sokcetPush: SokcetPush,
    private val mediaProjection: MediaProjection
) : Thread() {
    private lateinit var mediaCodec: MediaCodec

    companion object {
        private const val NAL_I = 19//i帧的值是19
        private const val NAL_VPS = 32//h265中的vps信息值是32
    }

    init {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            mediaCodec = MediaCodec.createEncoderByType(MediaFormat.MIMETYPE_VIDEO_HEVC)
            val mediaFormat =
                MediaFormat.createVideoFormat(MediaFormat.MIMETYPE_VIDEO_HEVC, 540, 960)
            mediaFormat.setInteger(
                MediaFormat.KEY_COLOR_FORMAT,
                MediaCodecInfo.CodecCapabilities.COLOR_FormatSurface
            );
            mediaFormat.setInteger(MediaFormat.KEY_FRAME_RATE, 20);//帧率，一秒15帧
            mediaFormat.setInteger(MediaFormat.KEY_BIT_RATE, 720 * 1280);//码率
            mediaFormat.setInteger(MediaFormat.KEY_I_FRAME_INTERVAL, 1);//2s一个I帧
            mediaCodec.configure(mediaFormat, null, null, MediaCodec.CONFIGURE_FLAG_ENCODE)
            //创建虚拟展示,name唯一的名字。dpi:1dpx=1px,
            //mediaCodec.createInputSurface(),提供一个虚拟的surface，和mediaProjection关联;mediaProjection会自动将yuv图片数据交给mediacodec，mediacodec又会自动交给dsp芯片编码
            mediaProjection.createVirtualDisplay(
                "screen",
                720,
                1280,
                1,
                DisplayManager.VIRTUAL_DISPLAY_FLAG_PUBLIC,
                mediaCodec.createInputSurface(),
                null,
                null
            )
        }
    }

    fun startCode() {
        Thread(this).start()
    }

    private lateinit var vps_sps_pps_buf: ByteArray

    override fun run() {
        super.run()
        mediaCodec.start()
        val bufferInfo = MediaCodec.BufferInfo()
        while (true) {
            try {
                val outputBufferId = mediaCodec.dequeueOutputBuffer(bufferInfo, 10000)
                if (outputBufferId >= 0) {
//                编码好的H265的数据
                    val byteBuffer: ByteBuffer? = mediaCodec.getOutputBuffer(outputBufferId)
                    dealFrame(byteBuffer, bufferInfo)
                    mediaCodec.releaseOutputBuffer(outputBufferId, false)
                }
            } catch (e: Exception) {
                e.printStackTrace()
                break
            }
        }
    }

    private fun dealFrame(
        bb: ByteBuffer?,
        bufferInfo: MediaCodec.BufferInfo
    ) {
        bb?.let {
            var offset = 4
            if (it[2].toInt() == 0x01) {
                offset = 3
            }
            //h264中，vps信息是6位，正好在中间，所以在计算值的时候需要右移1位
            val type: Int = (it[offset] and 0x7E).toInt() shr 1
            if (type == NAL_VPS) {
                vps_sps_pps_buf = ByteArray(bufferInfo.size)
                it[vps_sps_pps_buf]
            } else if (type == NAL_I) {
                val bytes = ByteArray(bufferInfo.size)
                it[bytes]
                val newBuf = ByteArray(vps_sps_pps_buf.size + bytes.size)
                System.arraycopy(vps_sps_pps_buf, 0, newBuf, 0, vps_sps_pps_buf.size)
                System.arraycopy(bytes, 0, newBuf, vps_sps_pps_buf.size, bytes.size)
                sokcetPush.sendData(newBuf)
            } else {
                val bytes = ByteArray(bufferInfo.size)
                it[bytes]
                sokcetPush.sendData(bytes)
            }
        }

    }
}
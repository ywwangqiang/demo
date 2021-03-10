package com.example.wangqiang.kotlin.h264

import android.hardware.display.DisplayManager
import android.media.MediaCodec
import android.media.MediaCodec.CONFIGURE_FLAG_ENCODE
import android.media.MediaCodec.createEncoderByType
import android.media.MediaCodecInfo
import android.media.MediaFormat
import android.media.projection.MediaProjection
import android.os.Build
import android.os.Environment
import java.io.FileOutputStream
import java.io.FileWriter
import java.io.IOException
import java.nio.ByteBuffer
import kotlin.experimental.and

/**
 * author : wangqiang
 * e-mail : wangqiang@geely.com
 * time   : 2021/01/09
 * desc   : 手机录屏，并且生成H264
 * version: 1.0
 */
class H264ScreenRecord(private var mediaProjection: MediaProjection) : Runnable {
    private var mediaCodec: MediaCodec = MediaCodec.createEncoderByType("video/avc")
    private var path: String =
        Environment.getExternalStorageDirectory().absolutePath + "/demo/record.h264"

    init {
        initMediaCodec()
    }

    private fun initMediaCodec() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            mediaCodec= createEncoderByType(MediaFormat.MIMETYPE_VIDEO_HEVC)
            val mediaFormat =
                MediaFormat.createVideoFormat(MediaFormat.MIMETYPE_VIDEO_AVC, 540, 960)
            mediaFormat.setInteger(
                MediaFormat.KEY_COLOR_FORMAT,
                MediaCodecInfo.CodecCapabilities.COLOR_FormatSurface
            );
            mediaFormat.setInteger(MediaFormat.KEY_FRAME_RATE, 15);//帧率，一秒15帧
            mediaFormat.setInteger(MediaFormat.KEY_BIT_RATE, 400_000);//码率
            mediaFormat.setInteger(MediaFormat.KEY_I_FRAME_INTERVAL, 2);//2s一个I帧
            mediaCodec.configure(mediaFormat, null, null, CONFIGURE_FLAG_ENCODE)
            //创建虚拟展示,name唯一的名字。dpi:1dpx=1px,
            //mediaCodec.createInputSurface(),提供一个虚拟的surface，和mediaProjection关联;mediaProjection会自动将yuv图片数据交给mediacodec，mediacodec又会自动交给dsp芯片编码
            mediaProjection.createVirtualDisplay(
                "screen",
                540,
                960,
                1,
                DisplayManager.VIRTUAL_DISPLAY_FLAG_PUBLIC,
                mediaCodec.createInputSurface(),
                null,
                null
            )
            while (true) {
                val bufferInfo =
                    MediaCodec.BufferInfo()
                val index = mediaCodec.dequeueOutputBuffer(bufferInfo, 100000)
                if (index >= 0) {
                    val buffer: ByteBuffer = mediaCodec.getOutputBuffer(index)
                    val outData = ByteArray(bufferInfo.size)
                    buffer.get(outData)
                    writeContent(outData) //以字符串的方式写入
                    //写成 文件  我们就能够播放起来
                    writeBytes(outData)
                    mediaCodec.releaseOutputBuffer(index, false)
                }
            }
        }

    }

    fun start() {
        mediaCodec.start()
        Thread(this).start()
    }

    fun writeBytes(array: ByteArray?) {
        var writer: FileOutputStream? = null
        try {
            // 打开一个写文件器，构造函数中的第二个参数true表示以追加形式写文件
            writer = FileOutputStream(
                path,
                true
            )
            writer.write(array)
            writer.write('\n'.toInt())
        } catch (e: IOException) {
            e.printStackTrace()
        } finally {
            try {
                if (writer != null) {
                    writer.close()
                }
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }

    fun writeContent(array: ByteArray): String? {
        val HEX_CHAR_TABLE = charArrayOf(
            '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'
        )
        val sb = StringBuilder()
        for (b in array) {
            sb.append(HEX_CHAR_TABLE[(b and 0xf0.toByte()).toInt() shr(4) ])
            sb.append(HEX_CHAR_TABLE[(b and 0x0f.toByte()).toInt()])
        }
        var writer: FileWriter? = null
        try {
            // 打开一个写文件器，构造函数中的第二个参数true表示以追加形式写文件
            writer = FileWriter(
                Environment.getExternalStorageDirectory().toString() + "/codec.txt", true
            )
            writer.write(sb.toString())
            writer.write("\n")
        } catch (e: IOException) {
            e.printStackTrace()
        } finally {
            try {
                writer?.close()
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
        return sb.toString()
    }
    override fun run() {}
}
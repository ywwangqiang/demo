package com.example.wangqiang.kotlin.h265.toupingplayer

import android.media.MediaCodec
import android.media.MediaFormat
import android.os.Build
import android.os.Bundle
import android.view.Surface
import android.view.SurfaceHolder
import androidx.appcompat.app.AppCompatActivity
import com.example.wangqiang.R
import kotlinx.android.synthetic.main.activity_touping_player.*
import java.nio.ByteBuffer


/**
 *     author : wangqiang
 *     e-mail : wangqiang@geely.com
 *     time   : 2021/01/26
 *     desc   : h265投屏接收端
 *     version: 1.0
 */
class TouPinPlayerActivity : AppCompatActivity(), SocketRecive.SocketCallback {
    private lateinit var surface: Surface
    private lateinit var mediaCodec: MediaCodec
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_touping_player)
        surfaceView.holder.addCallback(object : SurfaceHolder.Callback {
            override fun surfaceChanged(
                holder: SurfaceHolder?,
                format: Int,
                width: Int,
                height: Int
            ) {
                TODO("Not yet implemented")
            }

            override fun surfaceDestroyed(holder: SurfaceHolder?) {
                TODO("Not yet implemented")
            }

            override fun surfaceCreated(holder: SurfaceHolder) {
                surface = holder.surface
                initMediaCode(surface)
                initSocket()
            }

        })
    }

    fun initSocket() {
        SocketRecive(this).start()
    }

    override fun callBack(data: ByteArray) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            val index: Int = mediaCodec.dequeueInputBuffer(100000)
//index   索引
            //index   索引
            if (index >= 0) {
                val inputBuffer: ByteBuffer = mediaCodec.getInputBuffer(index)
                inputBuffer.clear()
                inputBuffer.put(data, 0, data.size)
                //       通知dsp芯片帮忙解码
                mediaCodec.queueInputBuffer(
                    index,
                    0, data.size, System.currentTimeMillis(), 0
                )
            }
            val bufferInfo: MediaCodec.BufferInfo = MediaCodec.BufferInfo()
            var outputBufferIndex: Int = mediaCodec.dequeueOutputBuffer(bufferInfo, 100000)

            while (outputBufferIndex > 0) {
                mediaCodec.releaseOutputBuffer(
                    outputBufferIndex, true
                )
                outputBufferIndex = mediaCodec.dequeueOutputBuffer(bufferInfo, 0)
            }
        }
    }

    fun initMediaCode(surface: Surface) {
        mediaCodec = MediaCodec.createDecoderByType(MediaFormat.MIMETYPE_VIDEO_HEVC);
        val mediaFormat =
            MediaFormat.createVideoFormat(MediaFormat.MIMETYPE_VIDEO_HEVC, 720, 1280)
        mediaFormat.setInteger(MediaFormat.KEY_FRAME_RATE, 20);//帧率，一秒15帧
        mediaFormat.setInteger(MediaFormat.KEY_BIT_RATE, 720 * 1280);//码率
        mediaCodec.configure(mediaFormat, surface, null, MediaCodec.CONFIGURE_FLAG_ENCODE)
    }

    override fun onDestroy() {
        super.onDestroy()
    }
}
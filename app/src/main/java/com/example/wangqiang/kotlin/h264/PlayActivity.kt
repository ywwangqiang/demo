package com.example.wangqiang.kotlin.h264

import android.os.Bundle
import android.os.Environment
import android.view.SurfaceHolder
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.wangqiang.R
import kotlinx.android.synthetic.main.activity_player.*

class PlayActivity : AppCompatActivity() {
    private var h264Frame: H264Frame? = null
    private var h264Player: H264Player? = null
    private val path = "/sdcard/demo/out.h264"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_player)
//        initSurface()
        initFame()
    }

    private fun initFame() {
        image.visibility = View.VISIBLE
        surface.visibility = View.GONE
        h264Frame = H264Frame(path, image)
        h264Frame?.play()
    }

    /**
     * mediacodec播放h264
     */
    private fun initSurface() {
        image.visibility = View.GONE
        surface.visibility = View.VISIBLE
        surface.holder.addCallback(object : SurfaceHolder.Callback {
            override fun surfaceChanged(
                holder: SurfaceHolder?,
                format: Int,
                width: Int,
                height: Int
            ) {

            }

            override fun surfaceDestroyed(holder: SurfaceHolder?) {
            }

            override fun surfaceCreated(holder: SurfaceHolder) {
                h264Player = H264Player(path, holder.surface)
                h264Player?.play()
            }

        })
    }

    override fun onDestroy() {
        super.onDestroy()
        h264Frame?.ondestory()
        h264Player?.ondestory()
    }
}
package com.example.wangqiang.kotlin.h264

import android.os.Bundle
import android.os.Environment
import android.view.SurfaceHolder
import androidx.appcompat.app.AppCompatActivity
import com.example.wangqiang.R
import kotlinx.android.synthetic.main.activity_player.*

class PlayActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_player)
        initSurface()
    }

    private fun initSurface() {
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
                val path =  "/sdcard/demo/out.h264"
                H264Player(path,holder.surface).play()
            }

        })
    }
}
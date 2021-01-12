package com.example.wangqiang.kotlin.h264

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.media.projection.MediaProjection
import android.media.projection.MediaProjectionManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.SurfaceHolder
import android.view.View
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.example.wangqiang.R
import kotlinx.android.synthetic.main.activity_player.*

class PlayActivity : AppCompatActivity() {
    private var h264Frame: H264Frame? = null
    private var h264Player: H264Player? = null
    private lateinit var mediaProjectionManager: MediaProjectionManager

    private val path = "/sdcard/demo/out.h264"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_player)
        initSurface()
//        initFame()
//        initProjection()
    }

    //mediacodec获取帧图片
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

    //手机录屏并保存到H264
    private fun initProjection() {
        checkPermission()
        tv_start_record.setOnClickListener {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                //获取录屏管理器
                mediaProjectionManager =
                    getSystemService(Context.MEDIA_PROJECTION_SERVICE) as MediaProjectionManager
                val intent = mediaProjectionManager.createScreenCaptureIntent()
                startActivityForResult(intent, 100)
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 100 && resultCode == Activity.RESULT_OK) {
            startRecord()
        }
    }

    /**
     *mediacodec需要提供一个虚拟的surface，和mediaprojection关联
     * mediaProjection将录制屏幕的数据丢给虚拟surface
     * mediacodec拿到虚拟surface的数据丢给dsp芯片进行编码
     */
    private fun startRecord() {

    }

    override fun onDestroy() {
        super.onDestroy()
        h264Frame?.ondestory()
        h264Player?.ondestory()
    }

    fun checkPermission(): Boolean {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && checkSelfPermission(
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            ) !== PackageManager.PERMISSION_GRANTED
        ) {
            requestPermissions(
                arrayOf(
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                ), 1
            )
        }
        return false
    }
}
package com.example.wangqiang

import android.app.Application
import com.example.wangqiang.kotlin.skin.SkinManager

class MyApplication : Application() {

    override fun onCreate() {
        super.onCreate()
//        SkinManager.instance.init(this)
    }
}
package com.example.wangqiang

import android.os.Bundle
import android.os.PersistableBundle
import android.view.Gravity
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import com.example.wangqiang.util.ScreenUtils

class DialogActivity :AppCompatActivity(){

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dialog)
        var lp=window.attributes
        lp.y=300
        lp.width=ViewGroup.LayoutParams.MATCH_PARENT
        lp.height=ScreenUtils.getScreenHeight(this)-300
        window.attributes=lp
        window.decorView.setPadding(0,0,0,0)
        window.setGravity(Gravity.TOP)
    }
}
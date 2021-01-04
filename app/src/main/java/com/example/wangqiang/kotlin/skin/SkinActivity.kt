package com.example.wangqiang.kotlin.skin

import android.os.Bundle
import android.view.LayoutInflater
import androidx.appcompat.app.AppCompatActivity
import com.example.wangqiang.R
import kotlinx.android.synthetic.main.layout_skin.*

class SkinActivity : AppCompatActivity() {
    private lateinit var skinFactory:SkinFactory

    override fun onCreate(savedInstanceState: Bundle?) {
        skinFactory=SkinFactory(delegate)
        layoutInflater.factory2 = skinFactory
        super.onCreate(savedInstanceState)
        setContentView(R.layout.layout_skin)
        tv_name.setOnClickListener {
            changSkin()
        }
    }

    private fun changSkin() {
        skinFactory.apply()
    }
}
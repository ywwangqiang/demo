package com.example.wangqiang.kotlin.base

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.wangqiang.DialogActivity

import com.example.wangqiang.R
import kotlinx.android.synthetic.main.layout_kotlint_test.*

class function : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.layout_kotlint_test)
//        logTest()
        text.setOnClickListener {
            var intent=Intent(this,DialogActivity::class.java)
            startActivity(intent)
        }
    }

    private fun logTest() {
        var names= listOf<String>("wang","qiang","test","java")
        var list= arrayListOf<Int>()
        list.maxBy { it }
        list.minBy { it }
//        names.forEach(print)
        names.forEach {
            print(it)
        }
    }

    var print = fun(content: String) {
        Log.e("tag123",content)
    }
}

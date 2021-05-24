package com.example.wangqiang.kotlin.base

import java.io.File

/**
 *     author : wangqiang
 *     e-mail : wangqiang@geely.com
 *     time   : 2021/05/09
 *     desc   : xxxx 描述
 *     version: 1.0
 */
fun main() {
    val configwordsFun = configWords()
    print(configwordsFun("2012"))
}

fun configWords(): (String) -> String {
    return {
        "$it year,"
    }
}

fun apply() {
    //apply函数提供可配置函数，返回当前对象
    val file = File("").apply {
        setReadable(true)
        setWritable(true)
    }
}

fun let() {
    //let函数返回lamb的最后一行
    val reslt = listOf(1, 2, 3).first().let {
        it * it
    }
}

//光看作用域，run和pply差不多，但与apply不同，run函数不返回接收者，run返回的是lambda结果，也就是true或者false
fun run() {
    val result = File("").run {
        readText().contains("great")
    }
}

fun with() {
    //with函数是run的变体，他们的功能行为是一样的，但with的调用方式不同，调用with时需要值参作为其第一个函数传入
}

//also函数和let函数功能相似，和let一样，also也是把接收者作为值参传给lambda，但不同的是：als返回接收者对象，而let返回lambda结果，因为这个差异，also尤其适合针对同一原始对象
//利用副作用做事，既然also返回的是接收者对象，你就可以基于原始接收者对象执行额外的链式调用
fun also() {
    var fileContents: List<String>
    File("").also {
        print(it.name)
    }.also {
        fileContents = it.readLines()
    }
}

fun takeif() {
    var content = File("").takeIf { it.exists() }?.readText()
}

fun takeUnless() {
    var content = File("").takeUnless { it.isHidden }?.readText()
}
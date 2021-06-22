package com.example.wangqiang.kotlin.base

import android.os.Build
import androidx.annotation.RequiresApi

/**
 *     author : wangqiang
 *     e-mail : wangqiang@geely.com
 *     time   : 2021/05/26
 *     desc   : xxxx 描述
 *     version: 1.0
 */
@RequiresApi(Build.VERSION_CODES.N)
fun main() {
//    val list = listOf("jack", "jason", "jacky")
//    println(list.getOrElse(4) { "unkonw" })
//    println(list.getOrNull(4) ?: "unkonw")

//    mutablelist()

    map()
}

@RequiresApi(Build.VERSION_CODES.N)
private fun mutablelist() {
    val mutableList = mutableListOf("jack", "jason", "jacky")
    mutableList.add("wang")
//    mutableList +="wang"
    mutableList.remove("wang")
//    mutableList -="wang"


    mutableList.removeIf { it.contains("jack") }
}

@RequiresApi(Build.VERSION_CODES.N)
private fun map() {
    val map = mutableMapOf("wang" to 20, "jack" to 30)
    val map1= mapOf(Pair("key","value"))
    println(map)
    map += "lisan" to 33  //map.put("lisan",33)
    println(map)

    map.getOrPut("lisan") { 30 }
    //get value
    map.getValue("wangqiang")//getvalue,读取key对应的值，如果不存在抛出异常
    map.getOrElse("wangqiang"){"unkonw"}//getOrElsef读取key对应的值，或者使用匿名函数返回默认值
    map.getOrDefault("wangqiang",40)
}


private fun array(){
    val intArray= intArrayOf(1,2,3)

    listOf(2,3,4).toIntArray()
}
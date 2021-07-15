@file:JvmName("otherName")//给类设置别名，必须在包之前,可以通过别名调用
package com.example.wangqiang.kotlin.base

/**
 *     author : wangqiang
 *     e-mail : wangqiang@geely.com
 *     time   : 2021/06/23
 *     desc   : xxxx 描述
 *     version: 1.0
 */
fun main() {
    val animals = listOf("zebra", "giraffe", "elephant", "rat")
    val babies = animals.map { "$it baby is" }
    print(babies)


    val nubmers = listOf(3, 5, 8, 10, 15)
    //寻找素数
    val results = nubmers.filter { number ->
        (2 until number).map { number % it }.none { it == 0 }
    }
    println(results)

    //合并操作符
    //zip
    val names = listOf("A", "B", "C")
    val ages = listOf(20, 30, 40)
    println(names.zip(ages))

    //fold 接收一个初始累加器，随后会根据匿名函数的结果更新
}

fun javaToKotlin() = "java is userd"
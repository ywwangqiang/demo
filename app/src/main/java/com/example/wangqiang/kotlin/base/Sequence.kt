package com.example.wangqiang.kotlin.base

/**
 *     author : wangqiang
 *     e-mail : wangqiang@geely.com
 *     time   : 2021/07/08
 *     desc   : xxxx 描述
 *     version: 1.0
 */
fun main() {
    val talList = (1..5000).toList().filter { it.isPrime() }.take(1000)
    println(talList.size)

    val sequenceList = generateSequence(2) {
        it + 1
    }.filter { it.isPrime() }.take(1000).toList()
    println(sequenceList.size)
}

fun Int.isPrime(): Boolean {
    (2 until this).map {
        if (this % it == 0) {
            return false;
        }
    }
    return true
}
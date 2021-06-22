package com.example.wangqiang.kotlin.base

/**
 *     author : wangqiang
 *     e-mail : wangqiang@geely.com
 *     time   : 2021/06/16
 *     desc   : 操作符重载
 *     + plus
 *     += plusAssign
 *     == equals
 *     > compareTo
 *     [] get
 *     .. rangeTo
 *     in contains
 *     version: 1.0
 */
data class operateFun(var x: Int, var y: Int) {

    operator fun plus(other: operateFun) = operateFun(x + other.x, y + other.y)
}

fun main() {
    var operateFun1: operateFun = operateFun(10, 20)
    var operateFun2 = operateFun(20, 20)
    print(operateFun1 + operateFun2)
}
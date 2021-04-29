package com.example.wangqiang.kotlin.base

import android.util.Log

/**
 *     author : wangqiang
 *     e-mail : wangqiang@geely.com
 *     time   : 2021/04/28
 *     desc   : xxxx 描述
 *     version: 1.0
 */
fun main() {
//    val total = "Missisippi".count {
//        it == 's'
//    }
//    System.out.println("total length : ${total}")

    //声明一个变量，等于一个函数，变量是有类型的，函数也是有类型的
    //函数的类型是由参数和返回值决定
    //除了极少数情况，匿名函数不需要return关键字来返回数据，匿名函数会隐式或自动的返回最后一行语句的结果
//    val blessingFunction: () -> String = {
//        val holiday = "new year"
//        "happy ${holiday}"
//    }
//    System.out.println(blessingFunction())


//    val blessingFunction: (String) -> String = {it ->
//        val holiday = "new year"
//        "$it $holiday"
//    }
//    System.out.println(blessingFunction("jack"))

    //最后返回是strng类型，可以自动推导，没有参数，可以省略类型
//    val blessingFunction = {
//        val holiday = "new year"
//        " $holiday"
//    }
//    System.out.println(blessingFunction())

    //可以省略类型，参数类型需要在后面定义
//    val blessingFunction = {name:String,year:Int ->
//        val holiday = "new year"
//        " $holiday"
//    }
//    System.out.println(blessingFunction("java",2018))

//    1
//    val showDiscunt = { goods: String, hour: Int ->
//        "$goods,时间为$hour"
//    }
//    showOnBoard("手机", showDiscunt)

//    2
//    showOnBoard("手机", { goods: String, hour: Int ->
//        "$goods,时间为$hour"
//    })

    //    3  当lamb表达式为最后一个，或者是唯一的参数，可以放在括号外面
    showOnBoard("手机") { goods: String, hour: Int ->
        "$goods,时间为$hour"
    }
}

fun showOnBoard(goods: String, showDiscunt: (String, Int) -> String) {
    val hour = (1..24).shuffled().last()
    System.out.println(showDiscunt(goods, hour))
}
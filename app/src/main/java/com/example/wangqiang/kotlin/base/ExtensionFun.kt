package com.example.wangqiang.kotlin.base

import kotlin.contracts.InvocationKind
import kotlin.contracts.contract
import com.example.wangqiang.kotlin.base.extesion.radomCreate as radom2 //生成别名

/**
 *     author : wangqiang
 *     e-mail : wangqiang@geely.com
 *     time   : 2021/06/22
 *     desc   : xxxx 描述
 *     version: 1.0
 */
class ExtensionFun {
    fun String.add(count: Int) = this + "!".repeat(count)

    //    fun String?.printWithDefaule(default: String) = print(this ?: default)
    infix fun String?.printWithDefaule(default: String) = print(this ?: default)
    //用infix修饰，接收者和函数之间的点操作以及参数的一对括号都可以不要。 printWithDefaule.printWithDefaule("abc) === conent printWithDefaule "abc"

    val String.numVowels
        get() = count { "aeiou".contains(it) }
}

fun main() {
    val content: String? = null

    val stringList = listOf<String>("wang", "zhang", "lisi")
//    print(stringList.radomCreate())
    print(stringList.radom2())//别名
}

/**
 * T.() -> Unit  匿名扩展函数，没有参数，返回unit
 * 加了T.(),针对泛型的扩展函数，可以在block中，隐式的调用this对象
 */
public inline fun <T> T.apply(block: T.() -> Unit): T {
    block()
    return this
}
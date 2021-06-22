package com.example.wangqiang.kotlin.base

fun main() {
//    var str = "wangqing"
//    str.capitalize()
//    str.hello()
    var list = listOf("wang", "a", "b", "qiang")
    print(list.findMax { it.length })
}

fun String.hello() {
    print("helle word")
}

fun <T, R : Comparable<R>> List<T>.findMax(block: (T) -> R): T {
    var maxElement = get(0)
    var maxValue = block(maxElement)
    for (element in this) {
        var currentValue = block(element)
        if (currentValue > maxValue) {
            maxValue = currentValue
            maxElement = element
        }
    }
    return maxElement
}


class Plaryer{
    var url = "http://baidu.com"
        get() = field.capitalize()
        set(value) {
            field=value.trim()
        }
}
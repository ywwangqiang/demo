package com.example.wangqiang.kotlin.base

/**
 *     author : wangqiang
 *     e-mail : wangqiang@geely.com
 *     time   : 2021/07/09
 *     desc   : xxxx 描述
 *     version: 1.0
 */
class User {
    @JvmField //可以直接被java中User.name调用，不再需要get方法
    var name: String = " be user by java"

    @JvmOverloads //生成重载函数，java调用时可以省略参数
    fun overloadMethod(paramster1: String = "left", paramseter2: String = "right") {
        print("overload methd:$paramster1,$paramseter2")
    }
}
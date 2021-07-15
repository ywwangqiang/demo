package com.example.wangqiang.kotlin.base;

/**
 * author : wangqiang
 * e-mail : wangqiang@geely.com
 * time   : 2021/07/09
 * desc   : xxxx 描述
 * version: 1.0
 */
class JavaUseKt {

    public static void main(String[] args) {
//       System.out.println(FuncationKt.javaToKotlin());
        System.out.println(otherName.javaToKotlin());

        User user = new User();
        System.out.println(user.name);
        user.overloadMethod("java left");
    }
}

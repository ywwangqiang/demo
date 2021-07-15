package com.example.wangqiang.kotlin.base.extesion

/**
 *     author : wangqiang
 *     e-mail : wangqiang@geely.com
 *     time   : 2021/06/22
 *     desc   : xxxx 描述
 *     version: 1.0
 */
fun <T> Iterable<T>.radomCreate(): T = this.shuffled().first()
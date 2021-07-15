package com.example.wangqiang.kotlin.base

import kotlinx.coroutines.*

/**
 *     author : wangqiang
 *     e-mail : wangqiang@geely.com
 *     time   : 2021/07/14
 *     desc   : xxxx 描述
 *     version: 1.0
 */
fun main1() {
    //GlobalScope受应用周期影响
    GlobalScope.launch {
        delay(1000)//delay是一个特殊的挂起函数，不会造成线程阻塞，但是会挂起线程，并且只能在协程中使用
        print("gloabl launch")
    }
    Thread.sleep(2000)
    print("main thread")

    //作为用户感觉上，阻塞和挂起是一样的
    //挂起：一般是主动的，由系统或程序发出，甚至于辅存中去（不释放cpu，可能释放内存，放在外村）
    //阻塞：一般是被动的，在抢占资源中得不到资源，被动的挂起在内存中，等待某种资源或信号量将它唤醒（释放cpu）

    //阻塞线程
    runBlocking {

    }
    //挂起函数
//    coroutineScope {
//
//    }
}


fun main2() = runBlocking {
    val job=GlobalScope.launch {
        delay(1000)
        println("gloabl launch")
    }
    println("main end")
    job.join()//等待直到协程job结束
}

//简化，可以在执行操作的作用域内指定启动协程
//而不是像通常使用的GlobalScope中启动，
//runBlocking协程构建器将main函数转换为协程，包括runBlocking在内的每个协程构建器都将CoroutinScope的实例 添加到其代码块所在的作用域中
//我们可以在这个作用域中启动协程而无需显示join，因为外部协程（事例中的runBlocking）
//直到在其作用域中启动的所有协程都执行完毕后才会结束
fun main() = runBlocking {
    launch {
        delay(1000)
        println("gloabl launch")
    }
    println("main end")
}
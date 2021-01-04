package com.example.wangqiang.kotlin.rxjava

import io.reactivex.Observable
import io.reactivex.Observer
import io.reactivex.disposables.Disposable

internal class Test {
    private fun merge() {
        Observable.create<Int> { emitter ->
            emitter.onNext(1)
            emitter.onNext(2)
            emitter.onNext(3)
        }.subscribe(object : Observer<Int> {
            override fun onSubscribe(d: Disposable) {}
            override fun onNext(integer: Int) {}
            override fun onError(e: Throwable) {}
            override fun onComplete() {}
        })
    }
}
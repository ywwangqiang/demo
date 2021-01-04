package com.example.wangqiang.kotlin.rxjava

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.wangqiang.R
import io.reactivex.Observable
import io.reactivex.ObservableOnSubscribe
import io.reactivex.Observer
import io.reactivex.disposables.Disposable
import kotlinx.android.synthetic.main.layout_rxjava.*

class RxJavaActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.layout_rxjava)
        init()
    }

    private fun init() {
        merge.setOnClickListener {
            merge()
        }
    }

    /**
     * 合并操作符
     */
    private fun merge() {
//        Observable.create<Int> {
//            it.onNext(1)
//            it.onNext(2)
//            it.onNext(3)
//            it.onComplete()
//        }.startWith(Observable.create {
//            it.onNext(10)
//            it.onNext(20)
//            it.onNext(30)
//            it.onComplete()
//        }).subscribe {
//            Log.e("tag123", "accept : $it")
//        }
//        Observable.create<Int> {
//            it.onNext(1)
//            it.onNext(2)
//            it.onNext(3)
//            it.onComplete()
//        }.concatWith(Observable.create {
//            it.onNext(10)
//            it.onNext(20)
//            it.onNext(30)
//            it.onComplete()
//        }).subscribe {
//            Log.e("tag123", "accept : $it")
//        }
//        Observable.concat(
//            Observable.just(1, 2),
//            Observable.just(10, 20),
//            Observable.just(100, 200)
//        )
//            .subscribe {
//                Log.e("tag123", "accept : $it")
//            }
//        Observable.create<Int> { emitter ->
//            emitter.onNext(1)
//            emitter.onNext(2)
//            emitter.onComplete()
//            emitter.onNext(3)
//        }
//            .subscribe { integer -> Log.e("tag123", "accept :$integer") }

        Observable.create<Int> { emitter ->
            emitter.onNext(1)
            emitter.onNext(2)
            emitter.onNext(3)
        }.subscribe(object : Observer<Int> {
            override fun onSubscribe(d: Disposable) {
                Log.e("tag123","on subscribe")
            }
            override fun onNext(integer: Int) {
                Log.e("tag123","on next $integer")
            }
            override fun onError(e: Throwable) {}
            override fun onComplete() {}
        })
    }
}
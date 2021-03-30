package com.example.wangqiang.kotlin.rxjava

import android.os.Bundle
import android.os.PersistableBundle
import android.util.Log
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.wangqiang.R
import io.reactivex.Observable
import io.reactivex.ObservableOnSubscribe
import io.reactivex.disposables.Disposable
import io.reactivex.observables.ConnectableObservable
import io.reactivex.schedulers.Schedulers

/**
 *     author : wangqiang
 *     e-mail : qiang.wang12@geely.com
 *     time   : 2020/12/31
 *     desc   :
 *     version:
 */
class ConnectableObservableActivity : AppCompatActivity() {
    private lateinit var mObservable: Observable<Int>
    private lateinit var mConnectObservable: ConnectableObservable<Int>
    private var mSourceData = arrayListOf<Int>()
    private lateinit var mDisposable1: Disposable
    private lateinit var mDisposable2: Disposable

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.layout_activity_connecttable)
        createColdSource()
    }

    private fun createColdSource() {
        mObservable = getSource()
        //.publish()将源Observable转换成为HotObservable，当调用它的connect方法后，无论此时有没有订阅者，源Observable都开始发送数据，订阅者订阅后将可以收到数据，并且订阅者解除订阅不会影响源Observable数据的发射。
        mConnectObservable = mObservable.publish();
        mConnectObservable.connect()
        findViewById<TextView>(R.id.tv_sub_1).setOnClickListener {
            startSubscribe1()
        }
        findViewById<TextView>(R.id.tv_sub_2).setOnClickListener {
            startSubscribe2()
        }
        findViewById<TextView>(R.id.tv_sub_cancel_1).setOnClickListener {
            disposeSubscribe1()
        }
        findViewById<TextView>(R.id.tv_sub_cancel_2).setOnClickListener {
            disposeSubscribe2()
        }
    }

    private fun getSource(): Observable<Int> {
        return Observable.create(ObservableOnSubscribe<Int> {
            try {
                var i = 0
                while (true) {
                    it.onNext(i)
                    Log.e("tag123", "source :$i")
                    Thread.sleep(1000)
                    i++
                }
            } catch (e: Exception) {

            }
        }).subscribeOn(Schedulers.io());
    }

    private fun updateMessage() {
        val content: StringBuilder = StringBuilder("源发射数据：")
        for (mSourceDatum in mSourceData) {
            content.append("$mSourceDatum,")
        }
        findViewById<TextView>(R.id.tv_source).setText(content.toString())
    }

    private fun startSubscribe1() {
//        mDisposable1 = mObservable.subscribe {
//            Log.e("tag123", "sub1 :$it")
//        }
        mDisposable1 = mConnectObservable.subscribe {
            Log.e("tag123", "sub1 :$it")
        }
    }

    private fun startSubscribe2() {
//        mDisposable2 = mObservable.subscribe {
//            Log.e("tag123", "sub2 :$it")
//        }
        mDisposable2 = mConnectObservable.subscribe {
            Log.e("tag123", "sub2 :$it")
        }
    }

    private fun disposeSubscribe1() {
        if (mDisposable1 != null) {
            mDisposable1.dispose()
        }
    }

    private fun disposeSubscribe2() {
        if (mDisposable2 != null) {
            mDisposable2.dispose()
        }
    }
}
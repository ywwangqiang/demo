package com.example.wangqiang.kotlin.h265.toupingplayer

import org.java_websocket.client.WebSocketClient
import org.java_websocket.handshake.ServerHandshake
import java.net.URI
import java.nio.ByteBuffer

/**
 *     author : wangqiang
 *     e-mail : wangqiang@geely.com
 *     time   : 2021/01/26
 *     desc   : xxxx 描述
 *     version: 1.0
 */
class SocketRecive constructor(private val socketCallback: SocketCallback) {
    private lateinit var webSocketClient: WebSocketClient

    fun start() {
        val url = URI("ws://192.168.31.1:12001")
        webSocketClient = MyWebSocketClient(url)
        webSocketClient.connect()
    }

    inner class MyWebSocketClient(serverURI: URI?) :
        WebSocketClient(serverURI) {
        override fun onOpen(serverHandshake: ServerHandshake) {
        }

        override fun onMessage(s: String) {}
        override fun onMessage(bytes: ByteBuffer) {
            val byteArray = ByteArray(bytes.remaining())
            bytes.get(byteArray)
            socketCallback.callBack(byteArray)
        }

        override fun onClose(i: Int, s: String, b: Boolean) {
        }

        override fun onError(e: Exception) {

        }
    }

    interface SocketCallback {
        fun callBack(data: ByteArray)
    }
}
package com.example.h265toupinsend

import android.media.projection.MediaProjection
import org.java_websocket.WebSocket
import org.java_websocket.handshake.ClientHandshake
import org.java_websocket.server.WebSocketServer
import java.lang.Exception
import java.net.InetSocketAddress


/**
 * author : wangqiang
 * e-mail : wangqiang@geely.com
 * time   : 2021/01/26
 * desc   : xxxx 描述
 * version: 1.0
 */
class SokcetPush {
    private var webSocket: WebSocket? = null

    fun start(mediaProjection: MediaProjection) {
        webSocketServer.start()
        H265Code(this, mediaProjection).startCode()
    }

    private val webSocketServer: WebSocketServer =
        object : WebSocketServer(InetSocketAddress(12001)) {

            override fun onOpen(conn: WebSocket?, handshake: ClientHandshake?) {
                webSocket = conn
            }

            override fun onClose(conn: WebSocket?, code: Int, reason: String?, remote: Boolean) {
                TODO("Not yet implemented")
            }

            override fun onMessage(conn: WebSocket?, message: String?) {
                TODO("Not yet implemented")
            }

            override fun onStart() {
                TODO("Not yet implemented")
            }

            override fun onError(conn: WebSocket?, ex: Exception?) {
                TODO("Not yet implemented")
            }

        }

    fun sendData(byteArray: ByteArray) {
        webSocket?.send(byteArray)
    }

    fun close() {
        webSocket?.close()
        webSocketServer.stop()
    }
}
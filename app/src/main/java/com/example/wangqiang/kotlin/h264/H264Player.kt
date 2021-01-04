package com.example.wangqiang.kotlin.h264

import android.media.MediaCodec
import android.media.MediaFormat
import android.os.Build
import android.util.Log
import android.view.Surface
import androidx.annotation.RequiresApi
import java.io.*

class H264Player(private var path: String, private var surface: Surface) : Runnable {
    //    private lateinit var path: String
//    private lateinit var surface: Surface
    private lateinit var mediaCodeC: MediaCodec

    //    constructor(path: String, surface: Surface) {
//        this.path = path
//        this.surface = surface
//        initPlayer()
//    }
    init {
        initPlayer()
    }

    private fun initPlayer() {
        mediaCodeC = MediaCodec.createDecoderByType("video/avc")
        val mediaFormat = MediaFormat.createVideoFormat("video/avc", 368, 384)
        mediaFormat.setInteger(MediaFormat.KEY_FRAME_RATE, 15)
        mediaCodeC.configure(mediaFormat, surface, null, 0)
    }

    fun play() {
        mediaCodeC.start()
        Thread(this).start()
    }

    override fun run() {
        val bytes = getBytes(path)
        val byteBuffers = mediaCodeC.inputBuffers
        var frameStartIndex: Int = 0
        bytes?.let {
            while (true) {
                val availabIndex = mediaCodeC.dequeueInputBuffer(10000)
                if (availabIndex >= 0) {
                    val byteBuffer = byteBuffers.get(availabIndex)
                    val nextFrameStart = getFrame(it, frameStartIndex+2)
                    byteBuffer.clear()
                    byteBuffer.put(bytes, frameStartIndex, nextFrameStart - frameStartIndex)
                    mediaCodeC.queueInputBuffer(
                        availabIndex,
                        0,
                        nextFrameStart - frameStartIndex,
                        0,
                        0
                    )
                    frameStartIndex = nextFrameStart
                } else {
                    continue
                }
                val info = MediaCodec.BufferInfo()
                val outPutIndex = mediaCodeC.dequeueOutputBuffer(info, 10000)
                if (outPutIndex > 0) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//                        val outPutByteBuffer=mediaCodeC.getOutputBuffer(outPutIndex)
                        mediaCodeC.releaseOutputBuffer(outPutIndex, true)
                    }
                }
            }
        }
    }

    @Throws(IOException::class)
    fun getBytes(path: String?): ByteArray? {
        val `is`: InputStream = DataInputStream(FileInputStream(File(path)))
        var len: Int = 0
        val size = 1024
        var buf: ByteArray
        val bos = ByteArrayOutputStream()
        buf = ByteArray(size)
        while (`is`.read(buf, 0, size).also({ len = it }) != -1) bos.write(buf, 0, len)
        buf = bos.toByteArray()
        return buf
    }

    fun getFrame(bytes: ByteArray, start: Int): Int {
        for (index in start..bytes.size) {
            if (bytes[index].toInt() == 0x00 && bytes[index + 1].toInt() == 0x00
                && bytes[index + 2].toInt() == 0x00 && bytes[index + 3].toInt() == 0x01
            ) {
                return index
            }
        }
        return -1
    }
}
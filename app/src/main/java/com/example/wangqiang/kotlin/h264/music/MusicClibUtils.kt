package com.example.wangqiang.kotlin.h264.music

import android.media.AudioFormat
import android.media.MediaCodec
import android.media.MediaExtractor
import android.media.MediaFormat
import android.os.Build
import android.os.Environment
import com.example.wangqiang.util.PcmToWavUtil
import java.io.File
import java.io.FileOutputStream
import java.nio.ByteBuffer

/**
 *     author : wangqiang
 *     e-mail : wangqiang@geely.com
 *     time   : 2021/03/08
 *     desc   : 音频剪辑工具
 *     version: 29
 */
class MusicClibUtils {

    fun clib(musicPath: String, startTime: Long, endTime: Long) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            //mp3是封装格式，需要MediaExtractor编码,获取到压缩数据
            val mediaExtractor: MediaExtractor = MediaExtractor()
            mediaExtractor.setDataSource(musicPath)
            //获取轨道数量
            val trackCount = mediaExtractor.trackCount
            //遍历每个轨道信息，找到音频的轨道
            var audioIndex = -1;
            var format: MediaFormat? = null;
            for (index in 0..trackCount) {
                //获取轨道的配置信息
                val currentFormat = mediaExtractor.getTrackFormat(index)
                val formatType = currentFormat.getString(MediaFormat.KEY_MIME)//格式的类型
                if (formatType.startsWith("audio/")) {
                    audioIndex = index
                    format = currentFormat
                    break
                }
            }
            if (audioIndex >= 0) {
                mediaExtractor.selectTrack(audioIndex)//对音频轨道进行操作
                mediaExtractor.seekTo(
                    startTime,
                    MediaExtractor.SEEK_TO_CLOSEST_SYNC
                )//定位到starttime时间

                //allocate,分配方式产生的内存开销是在JVM,allocateDirect分配方式产生的开销在JVM之外。
                // 当操作数据量非常小时，两种分配方式操作使用时间基本是同样的，第一种方式有时可能会更快，可是当数据量非常大时，另外一种方式会远远大于第一种的分配方式。
                val buffer = ByteBuffer.allocateDirect(100 * 1000)
                val mediaCodec =
                    MediaCodec.createDecoderByType(format!!.getString(MediaFormat.KEY_MIME))
                mediaCodec.configure(format, null, null, 0)
                mediaCodec.start()
                var bufferInfo = MediaCodec.BufferInfo()

                val pcmFile = File(Environment.getExternalStorageDirectory(), "out.pcm")
                val fileChannel = FileOutputStream(pcmFile).channel
                while (true) {
                    val availabIndex = mediaCodec.dequeueInputBuffer(10000)
                    if (availabIndex > 0) {
                        val sampleTime = mediaExtractor.sampleTime
                        if (sampleTime == -1L || sampleTime > endTime) {
                            break
                        } else if (sampleTime < startTime) {
                            mediaExtractor.advance()//前进到下一个数据
                        }
                        //通过mediaExtractor读取数据，此时得到的是mp3数据，通过 bytebuffer入参出参获取
                        //直接通过mediaExtractor获取数据，如果是视频，不再需要通过分隔符
                        bufferInfo.size = mediaExtractor.readSampleData(buffer, 0)
                        bufferInfo.presentationTimeUs = sampleTime
                        bufferInfo.flags = mediaExtractor.sampleFlags
                        val audioBytes = ByteArray(buffer.remaining())
                        //将获取到的音频数据，传给bytebuffer，然后再将bytebuffer交个dsp芯片解码
                        buffer?.get(audioBytes)
                        val byteBuffer = mediaCodec.getInputBuffer(availabIndex)
                        //清空之前的缓存数据
                        byteBuffer?.clear()
                        //讲音频数据放入到bytebuffer中
                        byteBuffer?.put(audioBytes)
                        mediaCodec.queueInputBuffer(
                            availabIndex,
                            0,
                            bufferInfo.size,
                            bufferInfo.presentationTimeUs,
                            bufferInfo.flags
                        )
                        //释放上一帧的数据，移动到下一帧
                        mediaExtractor.advance()
                    }

                    var outPutBufferIndex = mediaCodec.dequeueOutputBuffer(bufferInfo, 10000)
                    while (outPutBufferIndex > 0) {
                        val outBuffer = mediaCodec.getOutputBuffer(outPutBufferIndex)
                        fileChannel.write(outBuffer)
                        mediaCodec.releaseOutputBuffer(outPutBufferIndex, false)
                        outPutBufferIndex = mediaCodec.dequeueOutputBuffer(bufferInfo, 10000)
                    }
                }
                fileChannel.close()
                mediaExtractor.release()
                mediaCodec.stop()
                mediaCodec.release()

                val clibFile = File(Environment.getExternalStorageDirectory(), "out.mp3")
                PcmToWavUtil(
                    format.getString(MediaFormat.KEY_SAMPLE_RATE) as Int,
                    AudioFormat.CHANNEL_IN_STEREO,
                    format.getString(MediaFormat.KEY_CHANNEL_COUNT) as Int,
                    AudioFormat.ENCODING_PCM_16BIT
                ).pcmToWav(
                    pcmFile.getAbsolutePath()
                    , clibFile.getAbsolutePath()
                )
            }
        }

    }
}
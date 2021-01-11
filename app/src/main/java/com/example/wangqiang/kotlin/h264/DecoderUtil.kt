package com.example.wangqiang.kotlin.h264

import kotlin.experimental.and

/**
 *     author : wangqiang
 *     e-mail : wangqiang@geely.com
 *     time   : 2021/01/11
 *     desc   : 解析H264的sps和pps信息
 *     version: 1.0
 */
class DecoderUtil {

    /**
     *获取到指定字节数的10进制
     * @param startIndex 开始字节数
     * @param offset 偏移几个字节
     * @param byteArray 源字节数组
     */
    fun getBitValue(startIndex: Int, offset: Int, byteArray: ByteArray): Int {
        var value = 0;
        for (index in 0..offset) {
            value = value shl 1
            if ((byteArray[startIndex / 8] and ((0x80 shr (startIndex % 8)).toByte())).toInt() != 0) {
                value += 1;
            }
        }
        return value
    }
}
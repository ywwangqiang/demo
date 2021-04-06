package com.example.wangqiang.kotlin.h264.util

/**
 *     author : wangqiang
 *     e-mail : wangqiang@geely.com
 *     time   : 2021/01/12
 *     desc   : xxxx 描述
 *     version: 1.0
 */
object Util {
        //00 00 01分隔符
        fun is3Cut(index: Int, bytes: ByteArray): Boolean {
            return bytes[index].toInt() == 0x00 && bytes[index + 1].toInt() == 0x00
                    && bytes[index + 2].toInt() == 0x01
        }
        //00 00 00 01分隔符
        fun is4Cut(index: Int, bytes: ByteArray): Boolean {
            return bytes[index].toInt() == 0x00 && bytes[index + 1].toInt() == 0x00
                    && bytes[index + 2].toInt() == 0x00 && bytes[index + 3].toInt() == 0x01

            fun hexStringToByteArray(s: String): ByteArray {
                //十六进制转byte数组
                val len = s.length
                val bs = ByteArray(len / 2)
                var i = 0
                while (i < len) {
                    bs[i / 2] = ((Character.digit(
                        s[i],
                        16
                    ) shl 4) + Character.digit(s[i + 1], 16)).toByte()
                    i += 2
                }
                return bs
            }
        }
}
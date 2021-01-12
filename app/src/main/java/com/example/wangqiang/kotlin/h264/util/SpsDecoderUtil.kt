package com.example.wangqiang.kotlin.h264.util

import com.example.wangqiang.kotlin.h264.bean.VideoInfo
import kotlin.experimental.and

/**
 *     author : wangqiang
 *     e-mail : wangqiang@geely.com
 *     time   : 2021/01/11
 *     desc   : 解析H264的sps信息
 *     version: 1.0
 */
class SpsDecoderUtil(private val byteArray: ByteArray) {

    private var startbit: Int = 0

    init {
        //计算起始的分隔符类型，分隔符的字节没有意义，需要跳过
        if (Util.is3Cut(0, byteArray)) {
            startbit = 3 * 8
        } else if (Util.is4Cut(0, byteArray)) {
            startbit = 4 * 8
        }
    }

    fun startDecode(): VideoInfo? {
        if (startbit == 0) {
            return null
        }
        var videoInfo: VideoInfo = VideoInfo()
        val forbidden_zero_bit = getBitValue(1)
        videoInfo.forbidden_zero_bit = forbidden_zero_bit
        val nal_ref_idc = getBitValue(2)
        videoInfo.nal_ref_idc = nal_ref_idc
        val nal_unit_type = getBitValue(5)
        videoInfo.nal_unit_type = nal_unit_type
        if (nal_unit_type == 7) {
            //sps信息
            val profile_idc = getBitValue(8)
            videoInfo.profile_idc = profile_idc
//            当constrained_set0_flag值为1的时候，就说明码流应该遵循基线profile(Baseline profile)的所有约束.constrained_set0_flag值为0时，说明码流不一定要遵循基线profile的所有约束。
            //            当constrained_set0_flag值为1的时候，就说明码流应该遵循基线profile(Baseline profile)的所有约束.constrained_set0_flag值为0时，说明码流不一定要遵循基线profile的所有约束。
            val constraint_set0_flag: Int = getBitValue(1) //(h264[1] & 0x80)>>7;
            videoInfo.constraint_set0_flag = constraint_set0_flag
            // 当constrained_set1_flag值为1的时候，就说明码流应该遵循主profile(Main profile)的所有约束.constrained_set1_flag值为0时，说明码流不一定要遵
            // 当constrained_set1_flag值为1的时候，就说明码流应该遵循主profile(Main profile)的所有约束.constrained_set1_flag值为0时，说明码流不一定要遵
            val constraint_set1_flag: Int = getBitValue(1) //(h264[1] & 0x40)>>6;
            videoInfo.constraint_set1_flag = constraint_set1_flag
            //当constrained_set2_flag值为1的时候，就说明码流应该遵循扩展profile(Extended profile)的所有约束.constrained_set2_flag值为0时，说明码流不一定要遵循扩展profile的所有约束。
            //当constrained_set2_flag值为1的时候，就说明码流应该遵循扩展profile(Extended profile)的所有约束.constrained_set2_flag值为0时，说明码流不一定要遵循扩展profile的所有约束。
            val constraint_set2_flag: Int = getBitValue(1) //(h264[1] & 0x20)>>5;
            videoInfo.constraint_set2_flag = constraint_set2_flag
//            注意：当constraint_set0_flag,constraint_set1_flag或constraint_set2_flag中不只一个值为1的话，那么码流必须满足所有相应指明的profile约束。
            //            注意：当constraint_set0_flag,constraint_set1_flag或constraint_set2_flag中不只一个值为1的话，那么码流必须满足所有相应指明的profile约束。
            val constraint_set3_flag: Int = getBitValue(1) //(h264[1] & 0x10)>>4;
            videoInfo.constraint_set3_flag = constraint_set3_flag
//            4个零位
            //            4个零位
            val reserved_zero_4bits: Int = getBitValue(4)
            videoInfo.reserved_zero_4bits = reserved_zero_4bits
//            它指的是码流对应的level级
            //            它指的是码流对应的level级
            val level_idc: Int = getBitValue(8)
            videoInfo.level_idc = level_idc
//        0
            //        0
            val seq_parameter_set_id: Int = getGlunbuValue()
            videoInfo.seq_parameter_set_id = seq_parameter_set_id
            // chroma_format_idc 的值应该在 0到 3的范围内（包括 0和 3）  yuv420  yuv422 yuv 444

            // chroma_format_idc 的值应该在 0到 3的范围内（包括 0和 3）  yuv420  yuv422 yuv 444
            if (profile_idc === 100) {
//                hight
//                颜色位深   8  10  0
                val chroma_format_idc: Int = getGlunbuValue()
                videoInfo.chroma_format_idc = chroma_format_idc
                //                bit_depth_luma_minus8   视频位深   0 八位   1 代表10位
                val bit_depth_luma_minus8: Int = getGlunbuValue()
                videoInfo.bit_depth_luma_minus8 = bit_depth_luma_minus8
                val bit_depth_chroma_minus8: Int = getGlunbuValue()
                videoInfo.bit_depth_chroma_minus8 = bit_depth_chroma_minus8
//                qpprime_y_zero_transform_bypass_flag    占用1个bit,当前使用到的字符为0xAC，  y轴 0标志位
                val qpprime_y_zero_transform_bypass_flag: Int = getBitValue(1)
                videoInfo.qpprime_y_zero_transform_bypass_flag =
                    qpprime_y_zero_transform_bypass_flag
                //               缩放换标志位
                val seq_scaling_matrix_present_flag: Int = getBitValue(1)
                videoInfo.seq_scaling_matrix_present_flag = seq_scaling_matrix_present_flag
            }
            val log2_max_frame_num_minus4 = getGlunbuValue();
            videoInfo.log2_max_frame_num_minus4 = log2_max_frame_num_minus4
//确定播放顺序和解码顺序的映射
            val pic_order_cnt_type = getGlunbuValue();
            videoInfo.pic_order_cnt_type = pic_order_cnt_type
            val log2_max_pic_order_cnt_lsb_minus4 = getGlunbuValue();
            videoInfo.log2_max_pic_order_cnt_lsb_minus4 = log2_max_pic_order_cnt_lsb_minus4
//编码索引  码流顺序
            val num_ref_frames = getGlunbuValue();
            videoInfo.num_ref_frames = num_ref_frames
            val gaps_in_frame_num_value_allowed_flag = getBitValue(1);
            videoInfo.gaps_in_frame_num_value_allowed_flag = gaps_in_frame_num_value_allowed_flag
            val pic_width_in_mbs_minus1 = getGlunbuValue();
            videoInfo.pic_width_in_mbs_minus1 = pic_width_in_mbs_minus1
            val pic_height_in_map_units_minus1 = getGlunbuValue();
            videoInfo.pic_height_in_map_units_minus1 = pic_height_in_map_units_minus1
            val width = (pic_width_in_mbs_minus1 + 1) * 16;
            videoInfo.width = width
            val height = (pic_height_in_map_units_minus1 + 1) * 16;
            videoInfo.height = height
        }
        return videoInfo
    }

    /**
     *获取到指定字节数的10进制
     * @param startbit 指定位数,一个字节占8位，是字节数组里面的计算起始位数
     * @param size 偏移几个位数
     * @param byteArray 源字节数组
     * 00 00 00 01 67 42 00 0A 8D 8D 40 28 02 AD 35 05 02 02 07 84 42 29 C0
     * 假设startbit是34，说明我们起始位第34/8=5个字节的第二位 ：67，十六进制是0100 0011
     *      (前面4个字节32位)+0100 0011
     *那34就是第二位中的1，
     * 假设size=3，那就是计算100的值
     * 先默认value为0，左移1位，还是等于0
     * byteArray[startbit / 8 ]：获取到startbit位的字节数，一个字节占8，需要/8来获取到字节数的index，这里就是第5个字节中的67：0100 0011
     * 0x80 shr (startbit % 8 )：0x80是1000 0000,右移到当前字节的位数。假设startbit是34，说明是第个5字节的第二位
     *     0100 0011 (byteArray[startbit / 8 ])
     * and 0100 0000 (将0x80右移startbit % 8)
     *
     *
     * 得到需要计算的是100
     * 先计算1，value=1，第二次循环的时候，左移一位，就成了10=4，第三次循环就成了100=8，就可以得到最后的值
     */
    fun getBitValue(size: Int): Int {
        var value = 0;
        for (index in 0..size) {
            value = value shl 1
            //判断当前位是否为1，如果为1，value需要+1，后面循环就会左移
            if ((byteArray[startbit / 8] and ((0x80 shr (startbit % 8)).toByte())).toInt() != 0) {
                value += 1;
            }
            startbit++
        }
        return value
    }

    /**
     * 获取哥伦布编码
     * 1 获取代表长度的0的个数
     * 2 再从1开始，截取后面相应个数
     * 3 计算整个值，再减去1
     *
     * 0010 0101
     * 前面两个0代表长度，代表第一个1后面两位是长度
     * 那就是0010 0，计算0010 0的值，然后减去1
     */
    fun getGlunbuValue(): Int {
        var zeroNum = 0;
        while (startbit < byteArray.size * 8) {
            //如果当前为是0，计算个数，如果是1，则代表哥伦布编码结束，跳出循环
            if ((byteArray[startbit / 8] and ((0x80 shr (startbit % 8)).toByte())).toInt() != 0) {
                startbit++
                break
            }
            zeroNum++;
            startbit++
        }


        /**
         * 计算值，同[getBitValue]
         */
        var value = 0;
        for (index in 0..zeroNum) {
            value = value shl 1
            if ((byteArray[startbit / 8] and ((0x80 shr (startbit % 8)).toByte())).toInt() != 0) {
                value += 1;
            }
        }
        //0010 0 计算了后面两个0的值，需要将第一个1的值也算进去。1就是左移zeroNum个数
        val result: Int = (1 shl zeroNum) + value - 1
        return result
    }
}
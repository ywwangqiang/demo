package com.example.wangqiang.kotlin.h264.bean

/**
 *     author : wangqiang
 *     e-mail : wangqiang@geely.com
 *     time   : 2021/01/12
 *     desc   : 视频解析sps文件生成的类信息
 *     version: 1.0
 */
class VideoInfo {
    var forbidden_zero_bit: Int? = -1
    var nal_ref_idc: Int? = -1
    var nal_unit_type: Int? = -1
    var profile_idc: Int? = -1
    var constraint_set0_flag: Int? = -1 //(h264[1] & 0x80)>>7;
    var constraint_set1_flag: Int? = -1 //(h264[1] & 0x40)>>6;
    var constraint_set2_flag: Int? = -1 //(h264[1] & 0x20)>>5;
    var constraint_set3_flag: Int? = -1 //(h264[1] & 0x10)>>4;
    var reserved_zero_4bits: Int? = -1
    var level_idc: Int? = -1
    var seq_parameter_set_id: Int? = -1
    var chroma_format_idc: Int? = -1
    var bit_depth_luma_minus8: Int? = -1
    var bit_depth_chroma_minus8: Int? = -1
    var qpprime_y_zero_transform_bypass_flag: Int? = -1
    var seq_scaling_matrix_present_flag: Int? = -1
    var log2_max_frame_num_minus4: Int? = -1
    var pic_order_cnt_type: Int? = -1
    var log2_max_pic_order_cnt_lsb_minus4: Int? = -1
    var num_ref_frames: Int? = -1
    var gaps_in_frame_num_value_allowed_flag: Int? = -1
    var pic_width_in_mbs_minus1: Int? = -1
    var pic_height_in_map_units_minus1: Int? = -1
    var width: Int? = -1
    var height: Int? = -1
}
package com.example.wangqiang.kotlin.skin

import android.os.Build
import android.view.View
import android.widget.ImageView
import android.widget.TextView

class SkinView() {
    lateinit var skinItems: ArrayList<SkinItem>
    lateinit var view: View

    fun apply() {
        for (skinItem in skinItems) {
            if (skinItem.attriName.equals("background")) {
                if(skinItem.typeName.equals("color")){
                    view.setBackgroundColor(SkinManager.instance.getColor(skinItem.resId))
                }else if(skinItem.typeName.equals("drawable")){
                   if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.JELLY_BEAN){
                        view.setBackground(SkinManager.instance.getDrawable(skinItem.resId));
                    }else{
                        view.setBackgroundDrawable(SkinManager.instance.getDrawable(skinItem.resId));
                    }
                }
            }else if(skinItem.attriName.equals("src")){
                if(skinItem.typeName.equals("drawable") || skinItem.typeName.equals("mipmap")){
                    //将资源ID  传给ResouceManager  去进行资源匹配   如果匹配到了  就直接设置给控件
                    // 如果没有匹配到  就把之前的资源ID  设置控件
                    (view as ImageView).setImageDrawable(SkinManager.instance.getDrawable(skinItem.resId));
                }else if(skinItem.typeName.equals("color")){
//                    ((ImageView)view).setImageResource(SkinManager.getInstance().getColor(skinItem.getResId()));
                }
            }else if(skinItem.attriName.equals("textColor")){
                (view as TextView).setTextColor(SkinManager.instance.getColor(skinItem.resId));
            }
        }
    }
}
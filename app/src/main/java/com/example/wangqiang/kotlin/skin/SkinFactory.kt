package com.example.wangqiang.kotlin.skin

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewStub
import androidx.annotation.IntegerRes
import androidx.appcompat.app.AppCompatDelegate
import com.example.wangqiang.R

class SkinFactory(private var delegate: AppCompatDelegate) : LayoutInflater.Factory2 {
    var skinViewList = arrayListOf<SkinView>()

    private var prefixs = arrayOf(
        "android.widget.",
        "android.view.",
        "android.webkit."
    )

    override fun onCreateView(
        parent: View?,
        name: String,
        context: Context?,
        attrs: AttributeSet?
    ): View? {
//        Log.e("tag123", "create view name:$name")
        var view = delegate.createView(parent, name, context!!, attrs!!)
        if (view == null) {
            val layoutInflater = LayoutInflater.from(context)
            if (name.contains(".")) {
                try {
                    view = layoutInflater.createView(name, null, attrs)
                } catch (e: Exception) {

                }
            } else {
                prefixs.forEach {
                    try {
                        view = layoutInflater.createView(name, it, attrs)
                    } catch (e: Exception) {

                    }
                    view?.let {
                        return@forEach
                    }
                }
            }
        }
        view?.let {
            collectView(it, context, attrs)
        }
        return view
    }

    fun collectView(view: View, context: Context, attrs: AttributeSet) {
        val typeArray = context.obtainStyledAttributes(attrs, R.styleable.skinable)
        val isSkin = typeArray.getBoolean(R.styleable.skinable_isSkin, false)
        if (isSkin) {
            var skinView = SkinView()
            var skinItems = arrayListOf<SkinItem>()
            for (index in 1 until attrs.attributeCount) {
                val attrName = attrs.getAttributeName(index)
                if (attrName.contains("background") || attrName.contains("textColor")
                    || attrName.contains("src")
                ) {
                    val resIdStr = attrs.getAttributeValue(index)
                    val resId: Int = Integer.parseInt(resIdStr.substring(1))
                    val typeName = context.resources.getResourceTypeName(resId)
                    val typeValue = context.resources.getResourceEntryName(resId)
                    Log.e(
                        "tag123",
                        "view : $view,attrName :$attrName,value :$resId,typename :$typeName,typeValue :$typeValue"
                    )
                    var skinItem = SkinItem(attrName, typeName, typeValue, resId)
                    skinItems.add(skinItem)
                }

            }
            skinView.skinItems = skinItems
            skinView.view = view
            skinViewList.add(skinView)
        }
    }

    override fun onCreateView(name: String?, context: Context?, attrs: AttributeSet?): View {
        TODO("Not yet implemented")
    }

    fun apply(){
        for (skinView in skinViewList) {
            skinView.apply()
        }
    }
}
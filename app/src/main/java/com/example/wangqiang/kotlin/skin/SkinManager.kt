package com.example.wangqiang.kotlin.skin

import android.content.Context
import android.content.pm.PackageManager
import android.content.res.AssetManager
import android.content.res.Resources
import android.graphics.drawable.Drawable
import android.os.Environment
import android.util.Log
import androidx.core.content.ContextCompat


class SkinManager private constructor() {
    val tag = SkinManager::class.java.simpleName
    private lateinit var context: Context
    private lateinit var resourece: Resources
    private lateinit var skinPackage: String

    private val skinPath = Environment.getExternalStorageDirectory().absolutePath + "/demo/skin.apk"

    fun init(context: Context) {
        this.context = context
        initResources()
    }

    companion object {
        val instance = SingletonHolder.holder
    }

    private object SingletonHolder {
        val holder = SkinManager()
    }

    fun getColor(resId: Int): Int {
        val resourceName = context.resources.getResourceEntryName(resId)
        val resourceType = context.resources.getResourceTypeName(resId)
        Log.e(tag, "resource name :$resourceName,resoucetype name :$resourceType")
        val colorId = resourece.getIdentifier(resourceName, resourceType, skinPackage)
        if (colorId <= 0) {
            return resId
        }
        return colorId;
    }

    fun getDrawable(id: Int): Drawable? {
//        if (resourceIsNull()) {
//            //获取到这个id在当前应用中的Drawable对象
//            return ContextCompat.getDrawable(context, id)
//        }
        //获取到资源id的类型
        val resourceTypeName = context.resources.getResourceTypeName(id)
        //获取到的就是资源id的名字
        val resourceEntryName = context.resources.getResourceEntryName(id)
        //就是colorAccent这个资源在外置APK中的id
        val identifier: Int =
            resourece.getIdentifier(resourceEntryName, resourceTypeName, skinPackage)
        return if (identifier == 0) {
            ContextCompat.getDrawable(context, id)
        } else resourece.getDrawable(identifier)
    }


    fun initResources() {
        val assetManager = AssetManager::class.java.newInstance()
        val method = assetManager.javaClass.getDeclaredMethod("addAssetPath", String::class.java)
        method.invoke(assetManager, skinPath)
//        resourece = Resources(
//            assetManager,
//            context.resources.displayMetrics,
//            context.resources.configuration
//        )
        val packageManager = context.packageManager
        val packageInfo =
            packageManager.getPackageArchiveInfo(skinPath, PackageManager.GET_ACTIVITIES)
        skinPackage = packageInfo.packageName
        resourece = packageManager.getResourcesForApplication(skinPackage)
    }

}
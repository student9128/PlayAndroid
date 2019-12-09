package com.kevin.playandroid.base

import android.app.Application
import android.content.Context
import com.kevin.playandroid.util.SPUtils
import com.kevin.playandroid.util.SharedPreferencesUtils

/**
 * Created by Kevin on 2019-11-22<br/>
 * Blog:http://student9128.top/
 * 公众号：竺小竹
 * Describe:<br/>
 */
class BaseApplication : Application() {
    companion object {
        var mContext: Context? = null
        fun getContext(): Context {
            return mContext!!
        }
    }

    override fun onCreate() {
        super.onCreate()
        mContext = applicationContext
        SPUtils.initSP(this)
//        SharedPreferencesUtils
    }
}
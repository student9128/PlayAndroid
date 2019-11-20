package com.kevin.playandroid.util

import android.util.Log
import com.kevin.playandroid.BuildConfig

/**
 * Created by Kevin on 2019-11-20<br/>
 * Blog:http://student9128.top/
 * 公众号：竺小竹
 * Describe:<br/>
 */
object LogUtils {
    fun printD(tag: String, msg: Any) {
        if (BuildConfig.DEBUG) {
            Log.d(tag, "$tag==>>>: $msg")
        }
    }

    fun printV(tag: String, msg: Any) {
        if (BuildConfig.DEBUG) {
            Log.v(tag, "$tag==>>>: $msg")
        }
    }

    fun printE(tag: String, msg: Any) {
        if (BuildConfig.DEBUG) {
            Log.e(tag, "$tag==>>>: $msg")
        }
    }

    fun printI(tag: String, msg: Any) {
        if (BuildConfig.DEBUG) {
            Log.i(tag, "$tag==>>>: $msg")
        }
    }

    fun printW(tag: String, msg: Any) {
        if (BuildConfig.DEBUG) {
            Log.w(tag, "$tag==>>>: $msg")
        }
    }

}
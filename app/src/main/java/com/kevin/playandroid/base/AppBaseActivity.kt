package com.kevin.playandroid.base

import androidx.appcompat.app.AppCompatActivity
import com.kevin.playandroid.util.LogUtils

/**
 * Created by Kevin on 2019-11-19<br/>
 * Blog:http://student9128.top/
 * 公众号：竺小竹
 * Describe:<br/>
 */
open class AppBaseActivity : AppCompatActivity() {
    var TAG = javaClass.simpleName!!

    fun printD(msg: Any) {
        LogUtils.printD(TAG, msg)
    }

    fun printI(msg: Any) {
        LogUtils.printI(TAG, msg)
    }

    fun printW(msg: Any) {
        LogUtils.printW(TAG, msg)
    }

    fun printE(msg: Any) {
        LogUtils.printE(TAG, msg)
    }

    fun printV(msg: Any) {
        LogUtils.printV(TAG, msg)
    }
}
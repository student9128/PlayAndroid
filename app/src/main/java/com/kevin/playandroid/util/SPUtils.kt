package com.kevin.playandroid.util

import android.content.Context
import android.content.SharedPreferences
import androidx.annotation.NonNull

/**
 * Created by Kevin on 2019-11-22<br/>
 * Blog:http://student9128.top/
 * 公众号：竺小竹
 * Describe:<br/>
 * init in BaseApplication
 */
object SPUtils {
    private lateinit var sp: SharedPreferences
    private lateinit var editor: SharedPreferences.Editor
    /**
     * init
     */
    fun initSP(context: Context) {
        sp = context.getSharedPreferences(context.packageName + "_preference", Context.MODE_PRIVATE)
        editor = sp.edit()

    }

    fun save(@NonNull key: String, value: Any) {
        when (value) {
            is String -> editor.putString(key, value).apply()
            is Int -> editor.putInt(key, value).apply()
            is Boolean -> editor.putBoolean(key, value).apply()
            is Long -> editor.putLong(key, value).apply()
            is Float -> editor.putFloat(key, value).apply()
            else -> error("Only primitive types can be stored in SharedPreferences")
        }
    }

    fun getString(@NonNull key: String): String {
        return sp.getString(key, "").toString()
    }

    fun getInt(@NonNull key: String): Int {
        return sp.getInt(key, 0)
    }

    fun getBoolean(@NonNull key: String): Boolean {
        return sp.getBoolean(key, false)
    }

    fun getLong(@NonNull key: String): Long {
        return sp.getLong(key, 0)
    }

    fun getFloat(@NonNull key: String): Float {
        return sp.getLong(key, 0).toFloat()
    }


}
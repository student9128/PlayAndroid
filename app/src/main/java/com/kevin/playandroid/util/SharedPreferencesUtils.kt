package com.kevin.playandroid.util

import android.content.Context
import android.content.SharedPreferences
import androidx.preference.PreferenceManager
import java.util.*
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

/**
 * Created by Kevin on 2019-12-09<br/>
 * Blog:http://student9128.top/
 * 公众号：竺小竹
 * Describe:<br/>
 */
class SharedPreferencesUtils {
    private lateinit var sp: SharedPreferences
    private lateinit var editor: SharedPreferences.Editor
    private lateinit var preferences: SharedPreferences
    fun initSP(context: Context) {
        preferences = PreferenceManager.getDefaultSharedPreferences(context)
        sp =
            context.getSharedPreferences(context.packageName + "_preference", Context.MODE_PRIVATE)
        editor = sp.edit()

    }
    object SPDelegates {
        fun any(value: Any) = object : ReadWriteProperty<SharedPreferencesUtils, Any?> {

            override fun getValue(thisRef: SharedPreferencesUtils, property: KProperty<*>): Any? {
                return when (value) {
                    is String -> thisRef.sp.getString(property.name, "")
                    is Int -> thisRef.sp.getInt(property.name, 0)
                    is Boolean -> thisRef.sp.getBoolean(property.name, false)
                    is Long -> thisRef.sp.getLong(property.name, 0)
                    is Float -> thisRef.sp.getFloat(property.name, 0f)
                    else -> null
                }
            }

            override fun setValue(
                thisRef: SharedPreferencesUtils,
                property: KProperty<*>,
                value: Any?
            ) {
                when (value) {
                    is String -> thisRef.editor.putString(property.name, value).apply()
                    is Int -> thisRef.editor.putInt(property.name, value).apply()
                    is Boolean -> thisRef.editor.putBoolean(property.name, value).apply()
                    is Long -> thisRef.editor.putLong(property.name, value).apply()
                    is Float -> thisRef.editor.putFloat(property.name, value).apply()
                    else -> error("Only primitive types can be stored in SharedPreferences")
                }
            }

        }

    }

}
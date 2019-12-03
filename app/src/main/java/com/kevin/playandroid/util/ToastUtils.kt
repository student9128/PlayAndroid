package com.kevin.playandroid.util

import android.content.Context
import android.view.View
import android.widget.Toast
import androidx.annotation.NonNull
import com.google.android.material.snackbar.Snackbar

/**
 * Created by Kevin on 2019-11-21<br/>
 * Blog:http://student9128.top/
 * 公众号：竺小竹
 * Describe:<br/>
 */
object ToastUtils {
    private var toast: Toast? = null
    private var snack:Snackbar?=null

    fun showToast(@NonNull context: Context, redId: Int) {
        showToast(context, context.getString(redId))
    }

    fun showToast(context: Context, msg: String) {
        LogUtils.printD("Home", "msg=$msg")
//        toast?.cancel()
        if (toast == null) {
            LogUtils.printD("Home", "msg sss=$msg")
            toast = Toast.makeText(context, msg, Toast.LENGTH_SHORT)
        }
        toast!!.setText(msg)
        toast!!.show()
    }
    fun showSnack(view: View,text:CharSequence){
        if (snack == null) {
        snack = Snackbar.make(view, text, Snackbar.LENGTH_SHORT)
        }
        snack!!.setText(text).show()
    }
}
package com.kevin.playandroid.util

import android.content.Context
import android.widget.Toast
import androidx.annotation.NonNull

/**
 * Created by Kevin on 2019-11-21<br/>
 * Blog:http://student9128.top/
 * 公众号：竺小竹
 * Describe:<br/>
 */
object ToastUtils {
    private var toast: Toast? = null

    fun showToast(@NonNull context: Context, redId: Int) {
        showToast(context, context.getString(redId))
    }

    fun showToast(context: Context, msg: String) {
        if (toast == null) {
            toast = Toast.makeText(context, msg, Toast.LENGTH_SHORT)
        }
        toast?.let {
            it.setText(msg)
            it.show()
        }
    }
}
package com.kevin.playandroid.util

import android.content.Context
import android.os.Build
import android.text.Html
import android.text.SpannableString
import android.text.Spanned
import android.text.style.ForegroundColorSpan
import androidx.core.content.ContextCompat
import com.kevin.playandroid.R

/**
 * Created by Kevin on 2019/11/28<br/>
 * Blog:http://student9128.top/
 * 公众号：竺小竹
 * Describe:<br/>
 */
object FormatUtils {
    fun spanString(context: Context, spanStr: String, start: Int, end: Int): SpannableString {
        val spannableString = SpannableString(spanStr)
        spannableString.setSpan(
            ForegroundColorSpan(
                ContextCompat.getColor(
                    context,
                    R.color.colorPrimary
                )
            ), start, end, Spanned.SPAN_INCLUSIVE_INCLUSIVE
        )

        return spannableString
    }

    fun formatHtml(text: String): Spanned {
        return if (Build.VERSION.SDK_INT >= 24) Html.fromHtml(
            text,
            Html.FROM_HTML_MODE_LEGACY
        ) else Html.fromHtml(text)

    }
}
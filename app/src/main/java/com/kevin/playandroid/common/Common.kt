package com.kevin.playandroid.common

import android.content.Context
import android.os.Build
import android.text.Html
import android.text.SpannableString
import android.text.Spanned
import android.text.style.ForegroundColorSpan
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.kevin.playandroid.R

/**
 * Created by Kevin on 2019-11-29<br/>
 * Blog:http://student9128.top/
 * 公众号：竺小竹
 * Describe:<br/>
 */
/**
 * https://carlrice.io/blog/better-smoothscrollto
 */
fun RecyclerView.betterSmoothScrollToPosition(targetItem: Int) {
    layoutManager?.apply {
        val maxScroll = 20
        when (this) {
            is LinearLayoutManager -> {
                val topItem = findFirstVisibleItemPosition()
                val distance = topItem - targetItem
                val anchorItem = when {
                    distance > maxScroll -> targetItem + maxScroll
                    distance < -maxScroll -> targetItem - maxScroll
                    else -> topItem
                }
                if (anchorItem != topItem) scrollToPosition(anchorItem)
                post {
                    smoothScrollToPosition(targetItem)
                }
            }
            else -> smoothScrollToPosition(targetItem)
        }
    }

}

fun formatHtml(text: String): Spanned {
    return if (Build.VERSION.SDK_INT >= 24) Html.fromHtml(
        text,
        Html.FROM_HTML_MODE_LEGACY
    ) else Html.fromHtml(text)

}

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
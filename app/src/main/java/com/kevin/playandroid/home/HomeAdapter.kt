package com.kevin.playandroid.home

import android.content.Context
import android.os.Build
import android.text.Html
import android.text.SpannableString
import android.text.Spanned
import android.text.style.ForegroundColorSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.kevin.playandroid.R
import kotlinx.android.synthetic.main.adapter_item_home.view.*

/**
 * Created by Kevin on 2019-11-25<br/>
 * Blog:http://student9128.top/
 * 公众号：竺小竹
 * Describe:<br/>
 */
class HomeAdapter(val context: Context) :
    PagedListAdapter<DataX, HomeAdapter.ViewHolder>(HomeItemCallback()) {
    companion object {
        class HomeItemCallback : DiffUtil.ItemCallback<DataX>() {
            override fun areItemsTheSame(oldItem: DataX, newItem: DataX): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: DataX, newItem: DataX): Boolean {
                return oldItem.title == newItem.title && oldItem.author == newItem.author && oldItem.id == newItem.id
            }

        }
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val title: TextView = itemView.tv_title
        val tag: TextView = itemView.tv_tag
        val author: TextView = itemView.tv_author
        val category: TextView = itemView.tv_category
        val time: TextView = itemView.tv_time
        val container: LinearLayout = itemView.ll_container
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeAdapter.ViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.adapter_item_home, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: HomeAdapter.ViewHolder, position: Int) {
        getItem(position)?.run {
            holder.title.text = formatHtml(title)

            val authorStr: String =
                if (author.isNotEmpty()) "作者：$author" else "分享人：$shareUser"
            val authorSpanStr =
                spanString(authorStr, if (author.isNotEmpty()) 3 else 4, authorStr.length)
            holder.author.text = authorSpanStr

            val categoryStr = "分类：${formatHtml(superChapterName)}/${formatHtml(chapterName)}"
            holder.category.text = spanString(categoryStr, 3, categoryStr.length)

            holder.time.text = "时间：$niceDate"
            if (tags.isNotEmpty()) {
                holder.tag.visibility = View.VISIBLE
                holder.tag.text = tags[0].name
            } else {
                holder.tag.visibility = View.GONE
            }
//            tags.forEach { c ->
//                var tv: TextView = TextView(context)
//                var lp: LinearLayout.LayoutParams = LinearLayout.LayoutParams(-2, -2)
//                lp.setMargins(0, 0, DisplayUtils.dip2px(context, 5f), 0)
//                tv.layoutParams = lp
//                tv.text = c.name
//                tv.setPadding(5, 5, 5, 5)
//                tv.setTextColor(ContextCompat.getColor(context, R.color.green))
//                tv.setBackgroundResource(R.drawable.bg_home_tag)
//                holder.container.addView(tv, 0)
//
//            }


        }
    }

    private fun spanString(spanStr: String, start: Int, end: Int): SpannableString {
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

    private fun formatHtml(text: String): Spanned {
        return if (Build.VERSION.SDK_INT >= 24) Html.fromHtml(
            text,
            Html.FROM_HTML_MODE_LEGACY
        ) else Html.fromHtml(text)

    }
}
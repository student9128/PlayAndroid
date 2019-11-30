package com.kevin.playandroid.home

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager.widget.ViewPager
import com.google.android.material.card.MaterialCardView
import com.kevin.playandroid.R
import com.kevin.playandroid.common.WebActivity
import com.kevin.playandroid.common.formatHtml
import com.kevin.playandroid.common.spanString
import com.kevin.playandroid.util.DisplayUtils
import com.kevin.playandroid.util.ToastUtils
import com.kevin.playandroid.view.AdapterDataObserverProxy
import com.zhpan.bannerview.BannerViewPager
import com.zhpan.bannerview.constants.PageStyle
import com.zhpan.bannerview.holder.HolderCreator
import kotlinx.android.synthetic.main.adapter_item_home.view.*

/**
 * Created by Kevin on 2019-11-25<br/>
 * Blog:http://student9128.top/
 * 公众号：竺小竹
 * Describe:<br/>
 */
class HomeAdapter(val context: Context, var bannerData: List<BannerData>) :
    PagedListAdapter<DataX, RecyclerView.ViewHolder>(HomeItemCallback()) {
    companion object {
        private const val ITEM_TYPE_HEADER = 100
        private const val ITEM_TYPE_FOOTER = 101

        class HomeItemCallback : DiffUtil.ItemCallback<DataX>() {
            override fun areItemsTheSame(oldItem: DataX, newItem: DataX): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: DataX, newItem: DataX): Boolean {
                return oldItem.id == newItem.id && oldItem.collect == newItem.collect
            }
        }
    }

    fun addBanner(d: List<BannerData>) {
        bannerData = d
        notifyDataSetChanged()
    }

    class ContentViewHolder(itemView: View, var context: Context) :
        RecyclerView.ViewHolder(itemView) {
        val title: TextView = itemView.tv_title
        val tag: TextView = itemView.tv_tag
        val author: TextView = itemView.tv_author
        val category: TextView = itemView.tv_category
        val time: TextView = itemView.tv_time
        val mcContainer: MaterialCardView = itemView.mc_container
        val new: TextView = itemView.tv_new
        fun bindTo(data: DataX?) {
            data?.let {
                title.text = formatHtml(it.title)
                val authorStr: String =
                    if (it.author.isNotEmpty()) "作者：${it.author}" else "分享人：${it.shareUser}"
                val authorSpanStr =
                   spanString(
                        context,
                        authorStr,
                        if (it.author.isNotEmpty()) 3 else 4,
                        authorStr.length
                    )
                author.text = authorSpanStr
//
                val categoryStr =
                    "分类：${formatHtml(it.superChapterName)}/${formatHtml(it.chapterName)}"
                category.text = spanString(context, categoryStr, 3, categoryStr.length)
                category.setOnClickListener {
                    ToastUtils.showSnack(category, "$categoryStr")
                }
                time.text = it.niceDate
                if (it.tags.isNotEmpty()) {
                    tag.visibility = View.VISIBLE
                    tag.text = it.tags[0].name
                } else {
                    tag.visibility = View.GONE
                }
                if (it.fresh) {
                    new.visibility = View.VISIBLE
                } else {
                    new.visibility = View.GONE
                }
            }
            mcContainer.setOnClickListener {
                val intent = Intent(context, WebActivity::class.java)
                intent.putExtra("url", data!!.link)
                context.startActivity(intent)
            }
        }
    }

    class HeaderViewHolder(itemView: View, var context: Context) :
        RecyclerView.ViewHolder(itemView) {
        var viewPager: BannerViewPager<BannerData, BannerViewHolder> =
            itemView.findViewById(R.id.view_pager)

        fun bindTo(d: List<BannerData>) {
            viewPager.setAutoPlay(true)
                .setInterval(5000)
                .setPageMargin(DisplayUtils.dp2px(10f))
                .setPageStyle(PageStyle.MULTI_PAGE_SCALE)
                .setHolderCreator(HolderCreator { BannerViewHolder() })
                .setIndicatorColor(
                    ContextCompat.getColor(context, R.color.gray),
                    ContextCompat.getColor(context, R.color.colorPrimary)
                )
                .setIndicatorMargin(0, 0, 0, DisplayUtils.dp2px(16f))
            viewPager.create(d)
        }

    }

    class FooterViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            ITEM_TYPE_HEADER -> {
                val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.adapter_home_header, parent, false)
                HeaderViewHolder(view, context)
            }
            ITEM_TYPE_FOOTER -> {
                val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.adapter_home_footer, parent, false)
                FooterViewHolder(view)
            }
            else -> {
                val view =
                    LayoutInflater.from(parent.context)
                        .inflate(R.layout.adapter_item_home, parent, false)
                ContentViewHolder(view, context)
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is HeaderViewHolder -> {
                holder.bindTo(bannerData)
            }
            is FooterViewHolder -> {
            }
            is ContentViewHolder -> {
                holder.bindTo(getDataItem(position))
            }
        }
//        getItem(position)?.run {
//            holder.title.text = formatHtml(title)
//
//            val authorStr: String =
//                if (author.isNotEmpty()) "作者：$author" else "分享人：$shareUser"
//            val authorSpanStr =
//                spanString(authorStr, if (author.isNotEmpty()) 3 else 4, authorStr.length)
//            holder.author.text = authorSpanStr
//
//            val categoryStr = "分类：${formatHtml(superChapterName)}/${formatHtml(chapterName)}"
//            holder.category.text = spanString(categoryStr, 3, categoryStr.length)
//
//            holder.time.text = niceDate
//            if (tags.isNotEmpty()) {
//                holder.tag.visibility = View.VISIBLE
//                holder.tag.text = tags[0].name
//            } else {
//                holder.tag.visibility = View.GONE
//            }
//            holder.mcContainer.setOnClickListener {
//                //                ToastUtils.showToast(context, "$position")
//                LogUtils.printD("HomeAdapter", "pos=$position")
//                ToastUtils.showSnack(holder.mcContainer, "$position")
//            }
    }

    override fun registerAdapterDataObserver(observer: RecyclerView.AdapterDataObserver) {
        super.registerAdapterDataObserver(AdapterDataObserverProxy(observer, 1))
    }

    private fun getDataItem(position: Int): DataX? {
        return getItem(position - 1)
    }

    override fun getItemCount(): Int {
        return super.getItemCount() + 2
    }

    override fun getItemViewType(position: Int): Int {
        return when (position) {
            0 -> ITEM_TYPE_HEADER
            itemCount - 1 -> ITEM_TYPE_FOOTER
            else -> super.getItemViewType(position)
        }
    }


    interface OnRecyclerItemClickListener {
        fun onRecyclerItemClick(position: Int)
    }
}
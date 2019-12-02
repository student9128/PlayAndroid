package com.kevin.playandroid.project

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.android.material.card.MaterialCardView
import com.kevin.playandroid.R
import com.kevin.playandroid.common.WebActivity
import com.kevin.playandroid.common.formatHtml
import com.kevin.playandroid.home.DataX
import kotlinx.android.synthetic.main.adapter_item_footer.view.*
import kotlinx.android.synthetic.main.adapter_item_project.view.*

/**
 * Created by Kevin on 2019-11-29<br/>
 * Blog:http://student9128.top/
 * 公众号：竺小竹
 * Describe:<br/>
 */
class ProjectAdapter(var context: Context, var data: MutableList<DataX>) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    companion object {
        private const val TYPE_NORMAL = 1
        private const val TYPE_FOOTER = 2
    }

    private var isEnd = false
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            TYPE_NORMAL -> {
                val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.adapter_item_project, parent, false)
                ViewHolder(view)
            }
            TYPE_FOOTER -> {
                val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.adapter_item_footer, parent, false)
                FooterViewHolder(view)
            }
            else -> {
                val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.adapter_item_project, parent, false)
                ViewHolder(view)
            }
        }
    }

    override fun getItemCount(): Int {
        return data.size + 1
    }

    fun update(d: List<DataX>) {
        data.addAll(d)
        notifyDataSetChanged()
    }

    fun toEnd() {
        isEnd = true
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (getItemViewType(position) == TYPE_NORMAL) {
            val dataX = data[position]
            val viewHolder = holder as ViewHolder
            viewHolder.run {
                title.text = formatHtml(dataX.title)
                desc.text = formatHtml(dataX.desc)
                author.text = dataX.author
                time.text = dataX.niceDate.substring(0, 10)
                fav.text = dataX.zan.toString()
                Glide.with(context).load(dataX.envelopePic)
                    .centerCrop()
                    .into(conver)
                mcContainer.setOnClickListener {
                    listener?.let { it.onContainerItemClick(position) }
                }
                llFav.setOnClickListener {
                    listener?.let {
                        it.onChildItemClick(
                            R.id.tv_favorite,
                            position
                        )
                    }
                }
            }
        }
        if (getItemViewType(position) == TYPE_FOOTER) {
            val footerViewHolder = holder as FooterViewHolder
            if (isEnd) {
                with(footerViewHolder) {
                    tvProgress.text = "已经到底啦"
                    progressbar.visibility = View.GONE
                }

            } else {
                with(footerViewHolder) {
                    tvProgress.text = "正在加载中..."
                    progressbar.visibility = View.VISIBLE
                }
                listener?.let {
                    it.onLoadMore()
                }
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when (position) {
            itemCount - 1 -> TYPE_FOOTER
            else -> {
                TYPE_NORMAL
            }
        }
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val title: TextView = itemView.tv_title
        val author: TextView = itemView.tv_author
        val time: TextView = itemView.tv_time
        val fav: TextView = itemView.tv_favorite
        val desc: TextView = itemView.tv_describe
        val container: LinearLayout = itemView.ll_container
        val mcContainer: MaterialCardView = itemView.mc_container
        val conver: ImageView = itemView.iv_cover
        val llFav:LinearLayout = itemView.ll_favorite

    }

    class FooterViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvProgress: TextView = itemView.tv_progress
        val progressbar: ProgressBar = itemView.pb_progress
    }

    interface OnRecyclerItemListener {
        fun onLoadMore()
        fun onContainerItemClick(position: Int)
        fun onChildItemClick(viewId: Int, position: Int)
    }

    private var listener: OnRecyclerItemListener? = null
    fun setOnRecyclerItemListener(l: OnRecyclerItemListener) {
        listener = l
    }
}
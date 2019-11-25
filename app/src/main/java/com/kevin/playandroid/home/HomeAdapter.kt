package com.kevin.playandroid.home

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
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
class HomeAdapter() :
    PagedListAdapter<DataX, HomeAdapter.ViewHolder>(HomeItemCallback()) {
    companion object {
        class HomeItemCallback : DiffUtil.ItemCallback<DataX>() {
            override fun areItemsTheSame(oldItem: DataX, newItem: DataX): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: DataX, newItem: DataX): Boolean {
                return oldItem.title == newItem.title
            }

        }
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val title: TextView = itemView.tv_title
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeAdapter.ViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.adapter_item_home, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: HomeAdapter.ViewHolder, position: Int) {
        getItem(position)?.run {
            holder.title.text = title
        }
    }

//    override fun getItemCount(): Int {
//        return data.size
//    }
}
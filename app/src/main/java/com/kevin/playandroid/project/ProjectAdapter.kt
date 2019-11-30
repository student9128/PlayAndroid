package com.kevin.playandroid.project

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.android.material.card.MaterialCardView
import com.kevin.playandroid.R
import com.kevin.playandroid.common.WebActivity
import com.kevin.playandroid.common.formatHtml
import com.kevin.playandroid.home.DataX
import kotlinx.android.synthetic.main.adapter_item_project.view.*

/**
 * Created by Kevin on 2019-11-29<br/>
 * Blog:http://student9128.top/
 * 公众号：竺小竹
 * Describe:<br/>
 */
class ProjectAdapter(var context: Context, var data: MutableList<DataX>) :
    RecyclerView.Adapter<ProjectAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProjectAdapter.ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.adapter_item_project, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    fun update(d: List<DataX>) {
        data.addAll(d)
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: ProjectAdapter.ViewHolder, position: Int) {
        val dataX = data[position]
        holder.run {
            title.text = formatHtml(dataX.title)
            desc.text= formatHtml(dataX.desc)
            author.text = dataX.author
            time.text = dataX.niceDate
            fav.text = dataX.zan.toString()
            Glide.with(context).load(dataX.envelopePic)
                .centerCrop()
                .into(conver)
            mcContainer.setOnClickListener {
                val intent = Intent(context, WebActivity::class.java)
                intent.putExtra("url", dataX.link)
                context.startActivity(intent)
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
        var conver: ImageView = itemView.iv_cover

    }
}
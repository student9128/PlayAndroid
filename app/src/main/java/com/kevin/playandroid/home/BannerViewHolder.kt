package com.kevin.playandroid.home

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.kevin.playandroid.R
import com.zhpan.bannerview.holder.ViewHolder

/**
 * Created by Kevin on 2019/11/27<br/>
 * Blog:http://student9128.top/
 * 公众号：竺小竹
 * Describe:<br/>
 */
class BannerViewHolder : ViewHolder<BannerData> {
    private lateinit var image: ImageView
    override fun onBind(context: Context?, data: BannerData?, position: Int, size: Int) {
        Glide.with(context!!)
            .load(data!!.imagePath)
            .into(image)
    }

    override fun createView(viewGroup: ViewGroup?, context: Context?, position: Int): View {
        val view =
            LayoutInflater.from(context).inflate(R.layout.adapter_item_banner, viewGroup, false)
        image = view.findViewById(R.id.banner_image)
        return view
    }
}
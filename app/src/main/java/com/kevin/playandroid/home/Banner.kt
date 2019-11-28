package com.kevin.playandroid.home

import com.google.gson.annotations.SerializedName

/**
 * Created by Kevin on 2019/11/27<br/>
 * Blog:http://student9128.top/
 * 公众号：竺小竹
 * Describe:<br/>
 */
data class Banner(
    @SerializedName("data")
    val `data`: List<BannerData>,
    val errorCode: Int,
    val errorMsg: String
)

data class BannerData(
    val desc: String,
    val id: Int,
    val imagePath: String,
    val isVisible: Int,
    val order: Int,
    val title: String,
    val type: Int,
    val url: String
)
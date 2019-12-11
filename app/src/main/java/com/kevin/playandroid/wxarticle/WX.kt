package com.kevin.playandroid.wxarticle

import com.google.gson.annotations.SerializedName

/**
 * Created by Kevin on 2019-12-10<br/>
 * Blog:http://student9128.top/
 * 公众号：竺小竹
 * Describe:<br/>
 */
data class WX(
    @SerializedName("data")
    val `data`: List<WXData>,
    val errorCode: Int,
    val errorMsg: String
)

data class WXData(
    val children: List<Any>,
    val courseId: Int,
    val id: Int,
    val name: String,
    val order: Int,
    val parentChapterId: Int,
    val userControlSetTop: Boolean,
    val visible: Int
)
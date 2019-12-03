package com.kevin.playandroid.home

import com.kevin.playandroid.sign.Data

data class ArticleList(
    val `data`: Data,
    val errorCode: Int,
    val errorMsg: String
)

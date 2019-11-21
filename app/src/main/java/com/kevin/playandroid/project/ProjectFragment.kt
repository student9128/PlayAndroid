package com.kevin.playandroid.project

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.kevin.playandroid.R
import com.kevin.playandroid.base.BaseFragment

/**
 * Created by Kevin on 2019-11-21<br/>
 * Blog:http://student9128.top/
 * 公众号：竺小竹
 * Describe:<br/>
 */
class ProjectFragment :BaseFragment() {
    override fun initView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_project,container,false)
    }
}
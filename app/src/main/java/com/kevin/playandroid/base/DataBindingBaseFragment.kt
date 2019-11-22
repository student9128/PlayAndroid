package com.kevin.playandroid.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding

/**
 * Created by Kevin on 2019-11-21<br/>
 * Blog:http://student9128.top/
 * 公众号：竺小竹
 * Describe:<br/>
 */
abstract class DataBindingBaseFragment<DB : ViewDataBinding> : AppBaseFragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var db: DB = DataBindingUtil.inflate(inflater, getLayoutResId(), container, false)
        initView(db)
        return db.root
    }

    abstract fun getLayoutResId(): Int
    abstract fun initView(bindView: DB)
}
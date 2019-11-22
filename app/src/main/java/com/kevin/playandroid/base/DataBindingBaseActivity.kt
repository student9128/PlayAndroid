package com.kevin.playandroid.base

import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding

/**
 * Created by Kevin on 2019-11-21<br/>
 * Blog:http://student9128.top/
 * 公众号：竺小竹
 * Describe:<br/>
 */
abstract class DataBindingBaseActivity<DB : ViewDataBinding> : AppBaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        var db: DB = DataBindingUtil.setContentView(this, getLayoutResId())
        initView(db)
    }

    abstract fun getLayoutResId(): Int
    abstract fun initView(bindView: DB)
}
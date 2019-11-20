package com.kevin.playandroid.base

import android.os.Bundle
import android.view.MenuItem
import kotlinx.android.synthetic.main.layout_tool_bar.*

/**
 * Created by Kevin on 2019-11-19<br/>
 * Blog:http://student9128.top/
 * 公众号：竺小竹
 * Describe:<br/>
 */
abstract class BaseActivity : AppBaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initView()
        initListener()
    }

    fun initToolbar() {
        setSupportActionBar(tool_bar)
        supportActionBar?.let {
            it.setDisplayHomeAsUpEnabled(true)
            it.setHomeButtonEnabled(true)
        }
    }

    //点击toolbar返回键，返回
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> onBackPressed()
        }
        return true
    }

    abstract fun initView()
    abstract fun initListener()
}
package com.kevin.playandroid.base

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.widget.Toolbar
import com.kevin.playandroid.R

/**
 * Created by Kevin on 2019-11-19<br/>
 * Blog:http://student9128.top/
 * 公众号：竺小竹
 * Describe:<br/>
 */
abstract class BaseActivity : AppBaseActivity() {
    var toolbar: Toolbar? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(getLayoutResId())
        initToolbar()
        initView()
        initListener()
    }

    private fun initToolbar() {
        toolbar = findViewById(R.id.tool_bar)
        setSupportActionBar(toolbar!!)
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

    abstract fun getLayoutResId(): Int
    abstract fun initView()
    abstract fun initListener()

    fun startNewActivity(clazz: Class<out BaseActivity>) {
        startActivity(Intent(this, clazz))
    }

}
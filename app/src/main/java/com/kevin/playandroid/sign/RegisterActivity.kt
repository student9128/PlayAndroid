package com.kevin.playandroid.sign

import android.view.MenuItem
import com.kevin.playandroid.R
import com.kevin.playandroid.base.BaseActivity
import com.kevin.playandroid.listener.ActivityCreateListener
import kotlinx.android.synthetic.main.layout_tool_bar.*

/**
 * Created by Kevin on 2019-11-21<br/>
 * Blog:http://student9128.top/
 * 公众号：竺小竹
 * Describe:<br/>
 */
class RegisterActivity : BaseActivity() {
    companion object {
        var listener: ActivityCreateListener? = null
        open fun setOnActivityCreatedListener(listener: ActivityCreateListener) {
            this.listener = listener
        }
    }

    override fun getLayoutResId(): Int {
        return R.layout.activity_register
    }

    override fun initView() {
        tool_bar.setTitle(R.string.menu_register)
    }

    override fun onResume() {
        super.onResume()
        listener!!.onActivityCreateListener(TAG)
    }

    override fun initListener() {
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> onBackPressed()
        }
        return true
    }
}
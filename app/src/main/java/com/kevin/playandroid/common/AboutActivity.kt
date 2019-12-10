package com.kevin.playandroid.common

import com.kevin.playandroid.R
import com.kevin.playandroid.base.BaseActivity
import com.kevin.playandroid.listener.ActivityCreateListener

/**
 * Created by Kevin on 2019-12-10<br/>
 * Blog:http://student9128.top/
 * 公众号：竺小竹
 * Describe:<br/>
 */
class AboutActivity : BaseActivity() {
    companion object {
        var listener: ActivityCreateListener? = null
        open fun setOnActivityCreatedListener(listener: ActivityCreateListener) {
            this.listener = listener
        }
    }

    override fun getLayoutResId(): Int {
        return R.layout.activity_about
    }

    override fun initView() {
        toolbar?.setTitle(R.string.menu_about)
    }

    override fun initListener() {
    }

    override fun onResume() {
        super.onResume()
        listener!!.onActivityCreateListener(TAG)
    }
}
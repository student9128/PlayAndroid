package com.kevin.playandroid.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.kevin.playandroid.common.Constants
import com.kevin.playandroid.util.SPUtils
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlin.coroutines.CoroutineContext

/**
 * Created by Kevin on 2019-11-20<br/>
 * Blog:http://student9128.top/
 * 公众号：竺小竹
 * Describe:<br/>
 */
abstract class BaseFragment : AppBaseFragment() {
    //    lateinit var job: Job()
//    override val coroutineContext: CoroutineContext
//        get() = job + Dispatchers.Main
    val isLogin: Boolean by lazy { SPUtils.getBoolean(Constants.KEY_LOGIN_STATE) }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
//        job = Job()
        return initView(
            inflater,
            container,
            savedInstanceState
        )
    }

    abstract fun initView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View
//
//    suspend fun loadData(){
//    }
}
package com.kevin.playandroid.base

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import com.kevin.playandroid.util.LogUtils
import com.kevin.playandroid.util.ToastUtils

/**
 * Created by Kevin on 2019-11-20<br/>
 * Blog:http://student9128.top/
 * 公众号：竺小竹
 * Describe:<br/>
 */
open class AppBaseFragment : Fragment() {
    var TAG: String = javaClass.simpleName
    var mActivity: FragmentActivity? = null
    override fun onAttach(context: Context) {
        super.onAttach(context)
        mActivity = activity
    }

    fun printD(msg: Any) {
        LogUtils.printD(TAG, msg)
    }

    fun printI(msg: Any) {
        LogUtils.printI(TAG, msg)
    }

    fun printW(msg: Any) {
        LogUtils.printW(TAG, msg)
    }

    fun printE(msg: Any) {
        LogUtils.printE(TAG, msg)
    }

    fun printV(msg: Any) {
        LogUtils.printV(TAG, msg)
    }

    /**
     * toast with message
     * @param msg the message
     */
    fun toast(msg: String) {
        ToastUtils.showToast(context!!.applicationContext, msg)
    }

    /**
     * toast with resId
     * @param resId the message's res id
     */
    fun toast(resId: Int) {
        ToastUtils.showToast(context!!.applicationContext, resId)
    }
}
package com.kevin.playandroid.nav

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ExpandableListView
import android.widget.LinearLayout
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProviders
import com.kevin.playandroid.R
import com.kevin.playandroid.base.BaseFragment
import com.kevin.playandroid.common.Constants
import com.kevin.playandroid.common.WebActivity

/**
 * Created by Kevin on 2019-11-21<br/>
 * Blog:http://student9128.top/
 * 公众号：竺小竹
 * Describe:<br/>
 */
class NavFragment : BaseFragment(), NavAdapter.OnChildItemClickListener {
    private lateinit var navModel: NavModel
    private lateinit var mListView: ExpandableListView
    private lateinit var adapter: NavAdapter
    private var mData: MutableList<NavData> = ArrayList()
    private lateinit var llProgress: LinearLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        navModel = ViewModelProviders.of(this).get(NavModel::class.java)
    }

    override fun initView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.fragment_nav, container, false)
        mListView = view.findViewById(R.id.listView)
        llProgress = view.findViewById(R.id.ll_progress)
        adapter = NavAdapter(mActivity!!, mData)
        mListView.setAdapter(adapter)
        navModel.getNav()
        navModel.getData().observe(this, Observer {
            adapter.update(it)
        })
        navModel.getLoadingStatus().observe(this, Observer {
            if ("success" == it["progress"]) {
                llProgress.visibility = View.GONE
                mListView.visibility=View.VISIBLE
            }else{
                llProgress.visibility = View.VISIBLE
                mListView.visibility=View.GONE
            }
        })
        adapter.setOnChildItemClickListener(this)
        return view
    }

    override fun onChildItemClick(groupPosition: Int, childPosition: Int) {
        val link = mData[groupPosition].articles[childPosition].link
        var intent = Intent(mActivity!!, WebActivity::class.java)
        intent.putExtra(Constants.WEB_URL, link)
        startActivity(intent)
    }
}
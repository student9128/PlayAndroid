package com.kevin.playandroid.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.paging.PagedList
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.kevin.playandroid.R
import com.kevin.playandroid.base.BaseFragment

/**
 * Created by Kevin on 2019-11-20<br/>
 * Blog:http://student9128.top/
 * 公众号：竺小竹
 * Describe:<br/>
 */
class HomeFragment : BaseFragment() {

    private lateinit var homeModel: HomeModel
    private var mRecyclerView: RecyclerView? = null
    private var mAdapter: HomeAdapter? = null
    private var mData: MutableLiveData<Home>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        homeModel = mActivity.run {
            ViewModelProviders.of(this@HomeFragment).get(HomeModel::class.java)
        }

    }

    override fun initView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.fragment_home, container, false)
        mRecyclerView = view.findViewById(R.id.recycler_view)
        val layoutManager = LinearLayoutManager(mActivity)
        mRecyclerView!!.layoutManager = layoutManager
        mAdapter = HomeAdapter()
        mRecyclerView!!.adapter = mAdapter
        val initPagedListBuilder = homeModel.initPagedListBuilder(homeModel)
        initPagedListBuilder.observe(this,
            Observer<PagedList<DataX>> { t -> mAdapter!!.submitList(t) })
        return view
    }


}
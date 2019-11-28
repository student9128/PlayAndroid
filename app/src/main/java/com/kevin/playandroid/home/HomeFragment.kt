package com.kevin.playandroid.home

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.paging.PagedList
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.google.android.material.floatingactionbutton.FloatingActionButton
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
    private lateinit var mRecyclerView: RecyclerView
    private lateinit var mAdapter: HomeAdapter
    private lateinit var llProgress: LinearLayout
    private lateinit var fab: FloatingActionButton
    private lateinit var x: PagedList<DataX>
    private lateinit var mSwipeRefreshLayout: SwipeRefreshLayout
    private var bannerData: List<BannerData> = ArrayList()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        homeModel = mActivity.run {
            ViewModelProviders.of(this@HomeFragment).get(HomeModel::class.java)
        }

    }

    @SuppressLint("RestrictedApi")
    override fun initView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.fragment_home, container, false)
        mSwipeRefreshLayout = view.findViewById(R.id.srl_refresh)
        mSwipeRefreshLayout.setColorSchemeResources(
            android.R.color.holo_red_light,
            android.R.color.holo_blue_light,
            android.R.color.holo_orange_light
        );
        mRecyclerView = view.findViewById(R.id.recycler_view)
        llProgress = view.findViewById(R.id.ll_progress)
        fab = view.findViewById(R.id.fab)
        fab.setOnClickListener { mRecyclerView.betterSmoothScrollToPosition(0) }
        fab.visibility = View.INVISIBLE
        val layoutManager = LinearLayoutManager(mActivity)
        mRecyclerView.layoutManager = layoutManager
        mAdapter = HomeAdapter(mActivity!!, bannerData)
        mRecyclerView.adapter = mAdapter
        mRecyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if (layoutManager.findFirstCompletelyVisibleItemPosition() == 0) {
                    fab.visibility = View.INVISIBLE
                }
            }
        })
        homeModel.getData().observe(this, Observer { t ->
            mAdapter!!.submitList(t)
            printD("size=${t.size}")
//            x = t
        })
        homeModel.getLoadingStatus().observe(this, Observer { t ->
            if ("Loaded" == t) {
                llProgress.visibility = View.GONE
                mRecyclerView.visibility = View.VISIBLE
            } else {
                llProgress.visibility = View.VISIBLE
                mRecyclerView.visibility = View.GONE
            }
        })
        homeModel.getBannerData()
        homeModel.getBannerLiveData().observe(this, Observer {
            mAdapter.addBanner(it)
        })
//        view.findViewById<Button>(R.id.btn).setOnClickListener {
//                        mAdapter!!.submitList(null)
//            homeModel.refresh().observe(this,
//                Observer { t -> mAdapter!!.submitList(t) })
//        }
        mSwipeRefreshLayout.setOnRefreshListener {
            mAdapter!!.submitList(null)
            homeModel.refresh().observe(this, Observer { t ->
                mAdapter!!.submitList(t)
                if (mSwipeRefreshLayout.isRefreshing) mSwipeRefreshLayout.isRefreshing = false
            })
        }
        return view
    }

    /**
     * https://carlrice.io/blog/better-smoothscrollto
     */
    private fun RecyclerView.betterSmoothScrollToPosition(targetItem: Int) {
        layoutManager?.apply {
            val maxScroll = 20
            when (this) {
                is LinearLayoutManager -> {
                    val topItem = findFirstVisibleItemPosition()
                    val distance = topItem - targetItem
                    val anchorItem = when {
                        distance > maxScroll -> targetItem + maxScroll
                        distance < -maxScroll -> targetItem - maxScroll
                        else -> topItem
                    }
                    if (anchorItem != topItem) scrollToPosition(anchorItem)
                    post {
                        smoothScrollToPosition(targetItem)
                    }
                }
                else -> smoothScrollToPosition(targetItem)
            }
        }
    }

}
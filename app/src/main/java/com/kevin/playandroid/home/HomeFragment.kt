package com.kevin.playandroid.home

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.kevin.playandroid.R
import com.kevin.playandroid.base.BaseFragment
import com.kevin.playandroid.common.CommonModel
import com.kevin.playandroid.common.Constants
import com.kevin.playandroid.common.betterSmoothScrollToPosition
import com.kevin.playandroid.util.SPUtils
import com.kevin.playandroid.util.ToastUtils

/**
 * Created by Kevin on 2019-11-20<br/>
 * Blog:http://student9128.top/
 * 公众号：竺小竹
 * Describe:<br/>
 */
class HomeFragment : BaseFragment(), HomeAdapter.OnRecyclerItemClickListener {

    private lateinit var homeModel: HomeModel
    private lateinit var commonModel: CommonModel
    private lateinit var mRecyclerView: RecyclerView
    private lateinit var mAdapter: HomeAdapter
    private lateinit var llProgress: LinearLayout
    private lateinit var fab: FloatingActionButton
    private lateinit var mSwipeRefreshLayout: SwipeRefreshLayout
    private var bannerData: List<BannerData> = ArrayList()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        homeModel = mActivity.run {
            ViewModelProviders.of(this@HomeFragment).get(HomeModel::class.java)
        }
        commonModel = ViewModelProviders.of(this@HomeFragment).get(CommonModel::class.java)

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
            refresh()
        }
        mAdapter.setOnRecyclerItemListener(this)


        return view
    }

    fun refresh() {
        mAdapter!!.submitList(null)
        homeModel.refresh().observe(this, Observer { t ->
            mAdapter!!.submitList(t)
            if (mSwipeRefreshLayout.isRefreshing) mSwipeRefreshLayout.isRefreshing = false
        })
    }

    override fun onChildItemClick(viewId: Int, position: Int, data: DataX?) {
        val isLogin = getBooleanSP(Constants.KEY_LOGIN_STATE)
        when (viewId) {
            R.id.iv_favorite -> {
                if (isLogin) {
                    val collect = data!!.collect
                    if (collect) {
                        commonModel.unCollectArticle(data!!.id, position)
                    } else {
                        commonModel.collectArticle(data!!.id, position)
                    }
                } else {
                    ToastUtils.showSnack(mRecyclerView, "请先登录")
                }
                commonModel.getLoadingStatus().observe(this, Observer {
                    if (it["progress"] == "success") {
                        val i = it["position"]
                        if (position == i!!.toInt()) {
                            val collect = data!!.collect
                            data!!.collect = !collect
                            if (collect) {
                                ToastUtils.showSnack(mRecyclerView, "取消收藏")
                            } else {
                                ToastUtils.showSnack(mRecyclerView, "收藏成功")

                            }
                            mAdapter.notifyItemChanged(position, "payload$position")
                        }
                    }else{
                        val text = it["errorMsg"]
                        printD("test==$text")
//                        ToastUtils.showSnack(mRecyclerView, text!!)
                    }
                })
            }
        }
    }


}
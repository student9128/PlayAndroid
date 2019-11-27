package com.kevin.playandroid.home

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.paging.PagedList
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
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
    private var mData: MutableLiveData<Home>? = null
    private lateinit var llProgress: LinearLayout
    private lateinit var fab: FloatingActionButton
    private lateinit var x: PagedList<DataX>
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
        mRecyclerView = view.findViewById(R.id.recycler_view)
        llProgress = view.findViewById(R.id.ll_progress)
        fab = view.findViewById(R.id.fab)
        fab.setOnClickListener { mRecyclerView.smoothScrollToPosition(0) }
        fab.visibility = View.INVISIBLE
        val layoutManager = LinearLayoutManager(mActivity)
        mRecyclerView.layoutManager = layoutManager
        mAdapter = HomeAdapter(mActivity!!)
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
//        view.findViewById<Button>(R.id.btn).setOnClickListener {
//                        mAdapter!!.submitList(null)
//            homeModel.refresh().observe(this,
//                Observer { t -> mAdapter!!.submitList(t) })
//        }
        return view
    }


}
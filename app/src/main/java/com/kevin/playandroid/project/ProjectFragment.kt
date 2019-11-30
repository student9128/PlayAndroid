package com.kevin.playandroid.project

import android.annotation.SuppressLint
import android.os.Bundle
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
import com.kevin.playandroid.common.betterSmoothScrollToPosition
import com.kevin.playandroid.home.DataX

/**
 * Created by Kevin on 2019-11-21<br/>
 * Blog:http://student9128.top/
 * 公众号：竺小竹
 * Describe:<br/>
 */
class ProjectFragment : BaseFragment() {
    private lateinit var projectModel: ProjectModel
    private lateinit var mRecyclerView: RecyclerView
    private lateinit var mAdapter: ProjectAdapter
    private lateinit var llProgress: LinearLayout
    private lateinit var fab: FloatingActionButton
    private lateinit var mSwipeRefreshLayout: SwipeRefreshLayout
    private var mData: MutableList<DataX> = ArrayList()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        projectModel = mActivity.run {
            ViewModelProviders.of(this@ProjectFragment).get(ProjectModel::class.java)
        }
    }

    @SuppressLint("RestrictedApi")
    override fun initView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.fragment_project, container, false)
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
        mAdapter = ProjectAdapter(mActivity!!, mData)
        mRecyclerView.adapter = mAdapter
        projectModel.getProjectList(0, true)
        projectModel.getData().observe(this, Observer {
            mAdapter.update(it)
        })
        projectModel.getLoadingStatus().observe(this, Observer {
            if ("Loaded" == it) {
                llProgress.visibility = View.GONE
                mRecyclerView.visibility = View.VISIBLE
            } else {
                llProgress.visibility = View.VISIBLE
                mRecyclerView.visibility = View.GONE
            }
        })
        return view
    }
}
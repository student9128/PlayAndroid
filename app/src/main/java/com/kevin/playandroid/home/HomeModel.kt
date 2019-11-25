package com.kevin.playandroid.home

import androidx.lifecycle.*
import androidx.paging.DataSource
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.kevin.playandroid.http.AppRetrofit
import com.kevin.playandroid.util.LogUtils
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext

/**
 * Created by Kevin on 2019-11-25<br/>
 * Blog:http://student9128.top/
 * 公众号：竺小竹
 * Describe:<br/>
 */
class HomeModel : ViewModel(), LifecycleObserver, CoroutineScope {
    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main
    private var data = MutableLiveData<PagedList<Home>>()
    var d: Home? = null

    fun initPagedListBuilder(homeModel: HomeModel): LiveData<PagedList<DataX>> {
        val config = PagedList.Config.Builder()
            .setPageSize(10)
            .setEnablePlaceholders(false)
            .build()
//        initPagedListBuilder(config)
        val dataSourceFactory = object : DataSource.Factory<Int, DataX>() {
            override fun create(): DataSource<Int, DataX> {
                return HomeDataSource(homeModel)
            }
        }
        val build = LivePagedListBuilder(dataSourceFactory, config).build()
        return build
    }


    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    fun onDestroy() {

    }
}
package com.kevin.playandroid.home

import androidx.lifecycle.*
import androidx.paging.DataSource
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList

/**
 * Created by Kevin on 2019-11-25<br/>
 * Blog:http://student9128.top/
 * 公众号：竺小竹
 * Describe:<br/>
 */
class HomeModel : ViewModel() {
    private var liveData: LiveData<PagedList<DataX>>
    private var homeLiveData: MutableLiveData<HomeDataSource> = MutableLiveData()
    private var mLoadingStatus: LiveData<String> = MutableLiveData()

    init {
        val config = PagedList.Config.Builder()
            .setPageSize(10)
            .setEnablePlaceholders(false)
            .build()
        liveData = initPagedListBuilder(config).build()
        mLoadingStatus = Transformations.switchMap(
            homeLiveData
        ) { input -> input!!.getProgressStatus() }

    }

    fun getLoadingStatus(): LiveData<String> {
        return mLoadingStatus
    }

    fun getData(): LiveData<PagedList<DataX>> {
        return liveData
    }


    private fun initPagedListBuilder(config: PagedList.Config): LivePagedListBuilder<Int, DataX> {
        val dataSourceFactory = object : DataSource.Factory<Int, DataX>() {
            override fun create(): DataSource<Int, DataX> {
                val homeDataSource = HomeDataSource(viewModelScope)
                homeLiveData.postValue(homeDataSource)
                return homeDataSource
            }

        }
        return LivePagedListBuilder<Int, DataX>(dataSourceFactory, config)
    }
}
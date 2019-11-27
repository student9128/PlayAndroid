package com.kevin.playandroid.home

import androidx.lifecycle.*
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
    private var mLoadingStatus: LiveData<String> = MutableLiveData()
    private var config: PagedList.Config = PagedList.Config.Builder()
        .setPageSize(10)
        .setEnablePlaceholders(false)
        .build()

    init {
        val homeDataSourceFactory = createFac()
        liveData = createLiveData(homeDataSourceFactory)
        mLoadingStatus = Transformations.switchMap(
            homeDataSourceFactory.getSourceLiveData()
        ) { input -> input!!.getProgressStatus() }

    }

    private fun createLiveData(homeDataSourceFactory: HomeDataSourceFactory) =
        LivePagedListBuilder<Int, DataX>(homeDataSourceFactory, config).build()

    private fun createFac(): HomeDataSourceFactory {
        return HomeDataSourceFactory()
    }

    /**
     * 在该方法里获取所有需要的结果
     */
    fun getResult() {

    }

    fun getLoadingStatus(): LiveData<String> {
        return mLoadingStatus
    }

    /**
     * 通过重建进行刷新
     */
    fun refresh(): LiveData<PagedList<DataX>> {
        return createLiveData(createFac())

    }

    fun getData(): LiveData<PagedList<DataX>> {
        return liveData
    }
}
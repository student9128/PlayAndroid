package com.kevin.playandroid.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.paging.DataSource

/**
 * Created by Kevin on 2019/11/27<br/>
 * Blog:http://student9128.top/
 * 公众号：竺小竹
 * Describe:<br/>
 */
class HomeDataSourceFactory : DataSource.Factory<Int, DataX>() {
    private var sourceLiveData: MutableLiveData<HomeDataSource> = MutableLiveData()
    override fun create(): DataSource<Int, DataX> {
        val homeDataSource = HomeDataSource()
        sourceLiveData.postValue(homeDataSource)
        return homeDataSource
    }
    fun getSourceLiveData():LiveData<HomeDataSource>{
        return sourceLiveData
    }
}
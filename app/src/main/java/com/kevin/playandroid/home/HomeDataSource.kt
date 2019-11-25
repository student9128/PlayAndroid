package com.kevin.playandroid.home

import androidx.paging.DataSource
import androidx.paging.PageKeyedDataSource
import androidx.paging.PagedList
import com.kevin.playandroid.http.AppRetrofit
import com.kevin.playandroid.util.LogUtils
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.coroutines.CoroutineContext

/**
 * Created by Kevin on 2019-11-25<br/>
 * Blog:http://student9128.top/
 * 公众号：竺小竹
 * Describe:<br/>
 */
class HomeDataSource(var homeModel: HomeModel) :
    PageKeyedDataSource<Int, DataX>(), CoroutineScope {
    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main

    override fun loadInitial(
        params: LoadInitialParams<Int>,
        callback: LoadInitialCallback<Int, DataX>
    ) {
        val httpService = AppRetrofit.appRetrofit.getHttpService()
        launch {
            val response = withContext(Dispatchers.IO) {
                httpService.getArticleListKtx(0)
            }
            if (response.isSuccessful) {
                val message = response.message()
                LogUtils.printD("HomeModel", "message=$message")
                val response = response.body()
                val datas = response?.data?.datas
                val curPage = response?.data?.curPage
                callback.onResult(datas!!, null, curPage!! + 1)
            }
        }

    }

    override fun loadAfter(params: LoadParams<Int>, callback: LoadCallback<Int, DataX>) {
        LogUtils.printD(
            "HomeDataSource",
            "params.key=${params.key},params.requestedLoadSize=${params.requestedLoadSize}"
        )
        val httpService = AppRetrofit.appRetrofit.getHttpService()
        launch {
            val response = withContext(Dispatchers.IO) {
                httpService.getArticleListKtx(params.key)
            }
            if (response.isSuccessful) {
                val message = response.message()
                LogUtils.printD("HomeModel", "message=$message")
                val response = response.body()
                val datas = response?.data?.datas
                val curPage = response?.data?.curPage
                callback.onResult(datas!!, params.key+1)
            }
        }
    }

    override fun loadBefore(params: LoadParams<Int>, callback: LoadCallback<Int, DataX>) {
    }

    init {
        val config = PagedList.Config.Builder()
            .setPageSize(10)
            .setEnablePlaceholders(false)
            .build()

    }

    var dataSourceFactory = object : DataSource.Factory<Int, DataX>() {
        override fun create(): DataSource<Int, DataX> {
            return HomeDataSource(homeModel)
        }

    }


}
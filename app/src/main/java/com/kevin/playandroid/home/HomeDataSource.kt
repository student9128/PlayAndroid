package com.kevin.playandroid.home

import androidx.lifecycle.MutableLiveData
import androidx.paging.PageKeyedDataSource
import com.kevin.playandroid.http.AppRetrofit
import com.kevin.playandroid.http.HttpService
import com.kevin.playandroid.util.LogUtils
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 * Created by Kevin on 2019-11-25<br/>
 * Blog:http://student9128.top/
 * 公众号：竺小竹
 * Describe:<br/>
 */
class HomeDataSource(private val scope: CoroutineScope) :
    PageKeyedDataSource<Int, DataX>() {
    private var httpService: HttpService = AppRetrofit.appRetrofit.getHttpService()
    private var progressStatus: MutableLiveData<String> = MutableLiveData()

    fun getProgressStatus(): MutableLiveData<String> {
        return progressStatus
    }

    override fun loadInitial(
        params: LoadInitialParams<Int>,
        callback: LoadInitialCallback<Int, DataX>
    ) {
        scope.launch {
            progressStatus.postValue("Loading")
            val response = withContext(Dispatchers.IO) {
                httpService.getArticleListKtx(0)
            }
            if (response.isSuccessful) {
                progressStatus.postValue("Loaded")
                val message = response.message()
                val response = response.body()
                val datas = response?.data?.datas
                val toString = response.toString()
                LogUtils.printD("Hello",toString)
                val curPage = response?.data?.curPage
                callback.onResult(datas!!, null, curPage!! + 1)
            } else {
                progressStatus.postValue("Failed")
            }
        }

    }

    override fun loadAfter(params: LoadParams<Int>, callback: LoadCallback<Int, DataX>) {
        scope.launch {
            val response = withContext(Dispatchers.IO) {
                httpService.getArticleListKtx(params.key)
            }
            if (response.isSuccessful) {
                val message = response.message()
                val response = response.body()
                val datas = response?.data?.datas
                callback.onResult(datas!!, params.key + 1)
            }
        }
    }

    override fun loadBefore(params: LoadParams<Int>, callback: LoadCallback<Int, DataX>) {
    }


}
package com.kevin.playandroid.project

import androidx.lifecycle.*
import com.kevin.playandroid.home.DataX
import com.kevin.playandroid.http.AppRetrofit
import com.kevin.playandroid.http.HttpService
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext

/**
 * Created by Kevin on 2019-11-29<br/>
 * Blog:http://student9128.top/
 * 公众号：竺小竹
 * Describe:<br/>
 */
class ProjectModel : ViewModel(), CoroutineScope, LifecycleObserver {
    private var httpService: HttpService = AppRetrofit.appRetrofit.getHttpService()
    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main
    private var progressStatus: MutableLiveData<String> = MutableLiveData()
    private var dataSource: MutableLiveData<List<DataX>> = MutableLiveData()
    private lateinit var proJob: Job
    fun getProjectList(page: Int, firstTime: Boolean = false) {
        proJob = launch {
            refreshProgressStatus(firstTime, "Loading")
            val response = withContext(Dispatchers.IO) {
                httpService.getProjectList(page)
            }
            if (response.isSuccessful) {
                refreshProgressStatus(firstTime, "Loaded")
                val body = response.body()
                val datas = body?.data?.datas
                dataSource.postValue(datas)
            } else {
                refreshProgressStatus(firstTime, "Failed")
            }

        }
    }

    private fun refreshProgressStatus(refresh: Boolean, v: String) {
        if (refresh) {
            progressStatus.postValue(v)
        }
    }

    fun getData(): MutableLiveData<List<DataX>> {
        return dataSource
    }

    fun getLoadingStatus(): LiveData<String> {
        return progressStatus
    }

    fun refresh() {
        getProjectList(0,true)
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    fun onDestroy() {
        proJob.cancel()
    }
}
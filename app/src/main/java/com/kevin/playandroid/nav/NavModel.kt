package com.kevin.playandroid.nav

import androidx.lifecycle.*
import com.kevin.playandroid.home.DataX
import com.kevin.playandroid.http.AppRetrofit
import com.kevin.playandroid.http.HttpService
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext

/**
 * Created by Kevin on 2019-12-10<br/>
 * Blog:http://student9128.top/
 * 公众号：竺小竹
 * Describe:<br/>
 */
class NavModel : ViewModel(), CoroutineScope, LifecycleObserver {
    private var httpService: HttpService = AppRetrofit.appRetrofit.getHttpService()
    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main
    private var progressStatus: MutableLiveData<Map<String, String?>> = MutableLiveData()
    private var dataSource: MutableLiveData<List<NavData>> = MutableLiveData()
    private lateinit var navJob: Job

    fun getNav() {
        navJob = launch {
            //            refreshProgressStatus(firstTime, "Loading")
            val response = withContext(Dispatchers.IO) {
                httpService.getNav()
            }
            if (response.isSuccessful) {
                val body = response.body()
                val data = body?.data
                val errorMsg = body?.errorMsg
                val errorCode = body?.errorCode
                if (data != null && errorCode == 0) {
                    var map: MutableMap<String, String?> = mutableMapOf()
                    map["progress"] = "success"
                    map["errorMsg"] = errorMsg
                    progressStatus.postValue(map)
                    dataSource.postValue(data)
                } else {
                    var map: MutableMap<String, String?> = mutableMapOf()
                    map["progress"] = "failed"
                    map["errorMsg"] = errorMsg
                    progressStatus.postValue(map)
                }
            } else {
                var map: MutableMap<String, String?> = mutableMapOf()
                map["progress"] = "failed"
                map["errorMsg"] = response.errorBody().toString()
                progressStatus.postValue(map)
            }
        }
    }
//    private fun refreshProgressStatus(v: String) {
//        if (refresh) {
//            progressStatus.postValue(v)
//        }
//    }
    fun getData(): MutableLiveData<List<NavData>> {
        return dataSource
    }

    fun getLoadingStatus(): LiveData<Map<String, String?>> {
        return progressStatus
    }

    fun refresh() {
        getNav()
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    fun onDestroy() {
        navJob.cancel()
    }
}
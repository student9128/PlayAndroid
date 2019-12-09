package com.kevin.playandroid.base

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import androidx.lifecycle.ViewModel
import com.kevin.playandroid.http.AppRetrofit
import com.kevin.playandroid.http.HttpService
import kotlinx.coroutines.*
import retrofit2.Response
import kotlin.coroutines.CoroutineContext

/**
 * Created by Kevin on 2019-12-04<br/>
 * Blog:http://student9128.top/
 * 公众号：竺小竹
 * Describe:<br/>
 */
abstract class BaseViewModel : ViewModel(), CoroutineScope, LifecycleObserver {
    var httpService: HttpService = AppRetrofit.appRetrofit.getHttpService()
    private val job = Job()
    private val jobList: MutableList<Job> = ArrayList()
    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + job

    fun loadData(block: suspend CoroutineScope.() -> Unit) {
        val launch = launch { block }
//        launch { withContext(Dispatchers.IO){} }

    }

    abstract fun <T> getData(): Response<T>
//        httpService.


    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    fun onDestroy() {
        job.cancel()
//        if (jobList.size > 0) {
//            for (j in jobList) {
//                j.cancel()
//            }
//        }
    }

}
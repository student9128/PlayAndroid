package com.kevin.playandroid.common

import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.kevin.playandroid.http.AppRetrofit
import com.kevin.playandroid.http.HttpService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.coroutines.CoroutineContext

/**
 * Created by Kevin on 2019-12-02<br/>
 * Blog:http://student9128.top/
 * 公众号：竺小竹
 * Describe:<br/>
 */
class CommonModel : ViewModel(), CoroutineScope, LifecycleObserver {
    private var httpService: HttpService = AppRetrofit.appRetrofit.getHttpService()
    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main
    private var progressStatus: MutableLiveData<Map<String, String?>> = MutableLiveData()
    fun collectArticle(id: Int, position: Int) {
        launch {
            val response = withContext(Dispatchers.IO) {
                httpService.collectArticle(id)
            }
            if (response.isSuccessful) {
                val body = response.body()
                val errorMsg = body?.errorMsg
                var map: MutableMap<String, String?> = mutableMapOf()
                map["position"] = position.toString()
                if (errorMsg!!.isEmpty()) {
                    map["progress"] = "success"
                    map["errorMsg"] = errorMsg
                } else {
                    map["progress"] = "failed"
                    map["errorMsg"] = errorMsg
                }
                progressStatus.postValue(map)

            } else {
                var map: MutableMap<String, String?> = mutableMapOf()
                map["position"] = ""
                map["progress"] = "failed"
                map["errorMsg"] = "请求失败"
                progressStatus.postValue(map)
            }
        }
    }

    fun unCollectArticle(id: Int, position: Int) {
        launch {
            val response = withContext(Dispatchers.IO) {
                httpService.unCollectArticle(id)
            }
            if (response.isSuccessful) {
                val body = response.body()
                val errorMsg = body?.errorMsg
                var map: MutableMap<String, String?> = mutableMapOf()
                map["position"] = position.toString()
                if (errorMsg!!.isEmpty()) {
                    map["progress"] = "success"
                } else {
                    map["progress"] = "failed"
                }
                progressStatus.postValue(map)

            } else {
                var map: MutableMap<String, String?> = mutableMapOf()
                map["position"] = ""
                map["progress"] = "failed"
                progressStatus.postValue(map)
            }
        }
    }


    fun getLoadingStatus(): LiveData<Map<String, String?>> {
        return progressStatus
    }
}
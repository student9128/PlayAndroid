package com.kevin.playandroid.sign

import androidx.lifecycle.*
import com.kevin.playandroid.common.Constants
import com.kevin.playandroid.http.AppRetrofit
import com.kevin.playandroid.http.HttpService
import com.kevin.playandroid.util.SPUtils
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext

/**
 * Created by Kevin on 2019-12-02<br/>
 * Blog:http://student9128.top/
 * 公众号：竺小竹
 * Describe:<br/>
 */
class SignModel : ViewModel(), CoroutineScope, LifecycleObserver {
    private var httpService: HttpService = AppRetrofit.appRetrofit.getHttpService()
    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main
    private lateinit var registerJob: Job
    private lateinit var loginJob: Job
    private var progressStatus: MutableLiveData<Map<String, String?>> = MutableLiveData()

    fun getLoadingStatus(): LiveData<Map<String, String?>> {
        return progressStatus
    }

    private fun store(key: String, value: Any) {
        SPUtils.save(key, value)
    }

    fun register(username: String, password: String, confirmPassword: String) {
        registerJob = launch {
            val response = withContext(Dispatchers.IO) {
                httpService.register(username, password, confirmPassword)
            }
            if (response.isSuccessful) {
                val body = response.body()
                val data = body?.data
                val errorMsg = body?.errorMsg
                var map: MutableMap<String, String?> = mutableMapOf()
                map["errorMsg"] = errorMsg
                map["progress"] = "success"
                progressStatus.postValue(map)
                if (data != null && errorMsg.isNullOrEmpty()) {
//                    with(data) {
//                        store(Constants.KEY_USER_NAME, data.username)
//                        store(Constants.KEY_USER_ID, id)
//                        store(Constants.KEY_NICK_NAME, nickname)
//                    }

                }
//                } else {
                //注册成功
//                    val errorMsg = body?.errorMsg
//                    var map: MutableMap<String, String?> = mutableMapOf()
//                    map["errorMsg"] = errorMsg
//                    map["progress"] = "hide"
//                }
            } else {
                var map: MutableMap<String, String?> = mutableMapOf()
                map["errorMsg"] = ""
                map["progress"] = "failed"
                progressStatus.postValue(map)
            }
        }
    }

    fun login(username: String, password: String) {
        loginJob = launch {
            val response = withContext(Dispatchers.IO) {
                httpService.login(username, password)
            }
            if (response.isSuccessful) {
                val body = response.body()
                val data = body?.data
                val errorMsg = body?.errorMsg
                var map: MutableMap<String, String?> = mutableMapOf()
                map["errorMsg"] = errorMsg
                map["progress"] = "success"
                progressStatus.postValue(map)
                if (data != null && errorMsg.isNullOrEmpty()) {
                    store(Constants.KEY_LOGIN_STATE, true)
                    store(Constants.KEY_USER_NAME, data.username)
                }

            } else {
                var map: MutableMap<String, String?> = mutableMapOf()
                map["errorMsg"] = ""
                map["progress"] = "failed"
                progressStatus.postValue(map)
            }
        }
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    fun onDestroy() {

    }
}
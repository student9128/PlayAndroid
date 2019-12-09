package com.kevin.playandroid.sign

import androidx.lifecycle.*
import com.kevin.playandroid.common.Constants
import com.kevin.playandroid.http.AppRetrofit
import com.kevin.playandroid.http.HttpService
import com.kevin.playandroid.util.LogUtils
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
    private var loginProgressStatus: MutableLiveData<Map<String, String?>> = MutableLiveData()
    private var logoutProgressStatus: MutableLiveData<Map<String, String?>> = MutableLiveData()

    fun getLoadingStatus(): LiveData<Map<String, String?>> {
        return progressStatus
    }

    fun getLoginLoadingStatus(): LiveData<Map<String, String?>> {
        return loginProgressStatus
    }

    fun getLogoutLoadingStatus(): LiveData<Map<String, String?>> {
        return logoutProgressStatus
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
                val errorCode = body?.errorCode
                var map: MutableMap<String, String?> = mutableMapOf()
                map["errorMsg"] = errorMsg
                map["progress"] = "success"
                progressStatus.postValue(map)
                if (data !=null && errorCode == 0) {
                    login(username, password)
//                    with(data) {
//                        store(Constants.KEY_USER_NAME, data.username)
//                        store(Constants.KEY_USER_ID, id)
//                        store(Constants.KEY_NICK_NAME, nickname)
//                    }

                } else {
                    var map: MutableMap<String, String?> = mutableMapOf()
                    map["errorMsg"] = errorMsg
                    map["progress"] = "failed"
                    loginProgressStatus.postValue(map)
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
                map["errorMsg"] = response.errorBody().toString()
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
                val errorCode = body?.errorCode
                var map: MutableMap<String, String?> = mutableMapOf()
                map["errorMsg"] = errorMsg
                map["progress"] = "success"
                loginProgressStatus.postValue(map)
                LogUtils.printD("SignModel","login===$body")
                if (data != null && errorCode==0) {
                    store(Constants.KEY_LOGIN_STATE, true)
                    store(Constants.KEY_USER_NAME, data.username)
                } else {
                    var map: MutableMap<String, String?> = mutableMapOf()
                    map["errorMsg"] = errorMsg
                    map["progress"] = "failed"
                    loginProgressStatus.postValue(map)
                }

            } else {
                var map: MutableMap<String, String?> = mutableMapOf()
                map["errorMsg"] = response.errorBody().toString()
                map["progress"] = "failed"
                loginProgressStatus.postValue(map)
            }
        }
    }

    fun logout() {
        launch {
            val response = withContext(Dispatchers.IO) {
                httpService.logout()
            }
            if (response.isSuccessful) {
                val body = response.body()
                val data = body?.data
                val errorMsg = body?.errorMsg
                val errorCode = body?.errorCode
                var map: MutableMap<String, String?> = mutableMapOf()
                map["errorMsg"] = errorMsg
                map["progress"] = "success"
                logoutProgressStatus.postValue(map)
                LogUtils.printD("SignModel", body.toString())
                if (data == null && errorCode == 0) {
                    store(Constants.KEY_LOGIN_STATE, false)
                    store(Constants.KEY_USER_NAME, "")
                }

            } else {
                var map: MutableMap<String, String?> = mutableMapOf()
                map["errorMsg"] = ""
                map["progress"] = "failed"
                logoutProgressStatus.postValue(map)
            }
        }
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    fun onDestroy() {

    }
}
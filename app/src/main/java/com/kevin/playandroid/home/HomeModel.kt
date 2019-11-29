package com.kevin.playandroid.home

import androidx.lifecycle.*
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.kevin.playandroid.http.AppRetrofit
import com.kevin.playandroid.http.HttpService
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext

/**
 * Created by Kevin on 2019-11-25<br/>
 * Blog:http://student9128.top/
 * 公众号：竺小竹
 * Describe:<br/>
 */
class HomeModel : ViewModel(), CoroutineScope, LifecycleObserver {
    private var httpService: HttpService = AppRetrofit.appRetrofit.getHttpService()
    private var liveData: LiveData<PagedList<DataX>>
    private var mLoadingStatus: LiveData<String> = MutableLiveData()
    private var bannerLiveData: MutableLiveData<List<BannerData>> = MutableLiveData()
    private lateinit var jobBanner: Job
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

    fun getBannerData() {
        jobBanner = launch {
            val response = withContext(Dispatchers.IO) {
                httpService.getBanner()
            }
            if (response.isSuccessful) {
                val response = response.body()
                val data = response?.data
                bannerLiveData.postValue(data)
            }
        }
    }

    fun getBannerLiveData(): MutableLiveData<List<BannerData>> {
        return bannerLiveData
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

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    fun onDestroy() {
        jobBanner.cancel()
    }
}
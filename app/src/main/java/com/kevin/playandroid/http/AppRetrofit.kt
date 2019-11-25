package com.kevin.playandroid.http

import com.kevin.playandroid.BuildConfig
import com.kevin.playandroid.common.Constants
import com.kevin.playandroid.util.SPUtils
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

/**
 * Created by Kevin on 2019-11-22<br/>
 * Blog:http://student9128.top/
 * 公众号：竺小竹
 * Describe:<br/>
 * 对网络框架Retrofit进行封装
 */
class AppRetrofit {
    companion object {
        const val BASE_URL = "https://www.wanandroid.com/"
        const val CONNECT_TIME_OUT = 30L
        const val READ_TIME_OUT = 60L

        val appRetrofit: AppRetrofit by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) {
            AppRetrofit()
        }
    }

    private var retrofit: Retrofit

    constructor() {
        retrofit = Retrofit.Builder()
            .client(initBuilder().build())
            .addConverterFactory(GsonConverterFactory.create())
//            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
//            .addCallAdapterFactory()
            .baseUrl(BASE_URL)
            .build()
    }

    fun getHttpService(httpService: (HttpService) -> Unit): HttpService {
        return appRetrofit.create(HttpService::class.java)
    }

    fun getHttpService(): HttpService {
        return appRetrofit.create(HttpService::class.java)
    }

    private fun <T> create(service: Class<T>): T {
        return retrofit.create(service)
    }

    /**
     * init okhttp client for retrofit
     */
    private fun initBuilder(): OkHttpClient.Builder {
        var interceptor = HttpLoggingInterceptor()
        interceptor.level = HttpLoggingInterceptor.Level.BODY

        var builder = OkHttpClient.Builder()
        builder.sslSocketFactory(
            SSLSocketFactoryUtils.createSSLSocketFactory(),
            SSLSocketFactoryUtils.createTrustAllManager()
        )
            .hostnameVerifier(SSLSocketFactoryUtils.TrustAllHostnameVerifier())
        if (BuildConfig.DEBUG) {
            builder.addInterceptor(interceptor)
        }
        builder.retryOnConnectionFailure(true)
            .connectTimeout(CONNECT_TIME_OUT, TimeUnit.SECONDS)
            .readTimeout(READ_TIME_OUT, TimeUnit.SECONDS)
            .addInterceptor(SaveCookieInterceptor())
            .addInterceptor(RequestCookiesInterceptor())
        return builder
    }

    class SaveCookieInterceptor : Interceptor {
        override fun intercept(chain: Interceptor.Chain): Response {
            val request = chain.request()
            val response = chain.proceed(request)
            val requestUrl = request.url().toString()
            val domain = request.url().host()
            if ((requestUrl.contains(Constants.KEY_LOGIN) || requestUrl.contains(Constants.KEY_REGISTER))
                && response.header(Constants.KEY_SET_COOKIE).isNullOrEmpty()
            ) {
                val cookies = response.headers(Constants.KEY_SET_COOKIE)
                val c = encodeCookie(cookies)
                saveCookie(requestUrl, domain, c)
            }
            return response
        }

        private fun encodeCookie(cookies: List<String>): String {
            val sb = StringBuilder()
            val set = HashSet<String>()
            cookies.map { cookie -> cookie.split(";").toTypedArray() }
                .forEach { c ->
                    c.filterNot { s ->
                        set.contains(s)
                    }
                        .forEach { set.add(it) }
                }
            val iterator = set.iterator()
            while (iterator.hasNext()) {
                val cookie = iterator.next()
                sb.append(cookie).append(";")
            }
            val lastIndexOf = sb.lastIndexOf(";")
            if (sb.length - 1 == lastIndexOf) {
                sb.deleteCharAt(lastIndexOf)
            }
            return sb.toString()
        }

        private fun saveCookie(url: String, domain: String, cookies: String) {
            if (url.isNotEmpty()) {
                SPUtils.save(url, cookies)
            }
            if (domain.isNotEmpty()) {
                SPUtils.save(domain, cookies)
            }
        }

    }

    class RequestCookiesInterceptor : Interceptor {
        override fun intercept(chain: Interceptor.Chain): Response {
            val request = chain.request()
            val builder = request.newBuilder()
            val domain = request.url().host()
            val cookie = getCookie(domain)
            if (cookie.isNotEmpty()) {
                builder.addHeader("Cookie", cookie)
            }
            return chain.proceed(builder.build())
        }

        private fun getCookie(domain: String): String {
            return if (domain.isNotEmpty()) {
                SPUtils.getString(domain)
            } else {
                ""
            }
        }

    }
}
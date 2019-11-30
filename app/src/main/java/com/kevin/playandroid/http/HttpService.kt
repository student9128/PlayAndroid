package com.kevin.playandroid.http

import com.kevin.playandroid.home.Banner
import com.kevin.playandroid.home.Home
import io.reactivex.Observable
import kotlinx.coroutines.Deferred
import retrofit2.Response
import retrofit2.http.*

/**
 * Created by Kevin on 2019-11-22<br/>
 * Blog:http://student9128.top/
 * 公众号：竺小竹
 * Describe:<br/>
 * 所需要调用的接口
 */
interface HttpService {

    @POST("user/register")
    @FormUrlEncoded
    fun register(
        @Field("username") username: String, @Field("password") pwd: String,
        @Field("repassword") repwd: String
    ): Observable<Map<String, Any>>

    @POST("user/login")
    @FormUrlEncoded
    fun login(
        @Field("username") username: String, @Field("password") pwd: String
    ): Observable<Map<String, Any>>

    @GET("user/logout")
    fun logout(): Observable<Map<String, Any>>

    @GET("article/list/{page}/json")
    suspend fun getArticleListKtx(@Path("page") page: Int): Response<Home>

    @GET("banner/json")
    suspend fun getBanner(): Response<Banner>

    @GET("article/listproject/{page}/json")
    suspend fun getProjectList(@Path("page") page: Int): Response<Home>
}
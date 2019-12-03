package com.kevin.playandroid.http

import com.kevin.playandroid.home.ArticleList
import com.kevin.playandroid.home.Banner
import com.kevin.playandroid.home.Home
import com.kevin.playandroid.sign.Sign
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
    suspend fun register(
        @Field("username") username: String, @Field("password") pwd: String,
        @Field("repassword") repwd: String
    ): Response<Sign>

    @POST("user/login")
    @FormUrlEncoded
    suspend fun login(
        @Field("username") username: String, @Field("password") pwd: String
    ): Response<Sign>

    @GET("user/logout")
    suspend fun logout(): Observable<Map<String, Any>>

    @GET("article/list/{page}/json")
    suspend fun getArticleListKtx(@Path("page") page: Int): Response<Home>

    @GET("banner/json")
    suspend fun getBanner(): Response<Banner>

    @GET("article/listproject/{page}/json")
    suspend fun getProjectList(@Path("page") page: Int): Response<Home>

    @POST("lg/collect/{id}/json")
    suspend fun collectArticle(@Path("id") id: Int):Response<ArticleList>
    @POST("lg/uncollect_originId/{id}/json")
    suspend fun unCollectArticle(@Path("id") id: Int):Response<ArticleList>
}
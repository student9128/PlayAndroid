package com.kevin.playandroid.http

import io.reactivex.Observable
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST

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
    fun logout():Observable<Map<String,Any>>
}
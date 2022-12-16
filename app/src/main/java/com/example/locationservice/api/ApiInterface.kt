package com.example.locationservice.api

import com.example.locationservice.models.*
import retrofit2.Call
import retrofit2.http.*

interface ApiInterface {
    @Headers("Content-Type: application/json")
    @POST("register")
    fun register(@Body user: RegisterRequest): Call<Any>

    @Headers("Content-Type: application/json")
    @POST("login")
    fun login(@Body user: LoginRequest): Call<Any>

    @GET("exists/{login}")
    fun exists(@Path("login") login: String?): Call<Any>

    @GET("friends/{query}")
    fun friends(@Path("query") query: String?): Call<Any>

    @Headers("Content-Type: application/json")
    @POST("update")
    fun update(@Body user: LoginResponse): Call<Any>
}
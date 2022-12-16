package com.example.locationservice.api

import com.example.locationservice.models.*
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class ApiService {
    fun register(registerRequest: RegisterRequest, onResult: (Any?) -> Unit){
        val retrofit = ServiceBuilder.buildService(ApiInterface::class.java)
        retrofit.register(registerRequest).enqueue(
            object : Callback<Any> {
                override fun onResponse(call: Call<Any>, response: Response<Any>) {
                    val gson = Gson()
                    if (response.isSuccessful) {
                        val successfulResp: RegisterResponse = gson.fromJson(gson.toJson(response.body()), RegisterResponse::class.java)
                        onResult(successfulResp)
                    } else {
                        val errorBody = response.errorBody() ?: return
                        val type = object : TypeToken<ErrorResponse>() {}.type
                        val errorResponse: ErrorResponse? = gson.fromJson(errorBody.charStream(), type)
                        onResult(errorResponse)
                    }
                }
                override fun onFailure(call: Call<Any>, t: Throwable) {
                    onResult(null)
                }
            }
        )
    }

    fun login(loginRequest: LoginRequest, onResult: (Any?) -> Unit){
        val retrofit = ServiceBuilder.buildService(ApiInterface::class.java)
        retrofit.login(loginRequest).enqueue(
            object : Callback<Any> {
                override fun onResponse(call: Call<Any>, response: Response<Any>) {
                    val gson = Gson()
                    if (response.isSuccessful) {
                        val successfulResp: LoginResponse = gson.fromJson(gson.toJson(response.body()), LoginResponse::class.java)
                        onResult(successfulResp)
                    } else {
                        val errorBody = response.errorBody() ?: return
                        val type = object : TypeToken<ErrorResponse>() {}.type
                        val errorResponse: ErrorResponse? = gson.fromJson(errorBody.charStream(), type)
                        onResult(errorResponse)
                    }
                }
                override fun onFailure(call: Call<Any>, t: Throwable) {
                    onResult(null)
                }
            }
        )
    }

    fun exists(login: String, onResult: (Any?) -> Unit){
        val retrofit = ServiceBuilder.buildService(ApiInterface::class.java)
        retrofit.exists(login).enqueue(
            object : Callback<Any> {
                override fun onResponse(call: Call<Any>, response: Response<Any>) {
                    val gson = Gson()
                    if (response.isSuccessful) {
                        val successfulResp: UserExistsResponse? = gson.fromJson(gson.toJson(response.body()), UserExistsResponse::class.java)
                        onResult(successfulResp)
                    } else {
                        val errorBody = response.errorBody() ?: return
                        println(errorBody)
                        val type = object : TypeToken<ErrorResponse>() {}.type

                        val errorResponse: ErrorResponse? = gson.fromJson(errorBody.charStream(), type)
                        onResult(errorResponse)
                    }
                }
                override fun onFailure(call: Call<Any>, t: Throwable) {
                    onResult(null)
                }
            }
        )
    }

    //NOTE: the request is a LoginResponse because a user is represented as a LoginResponse
    fun update(logResp: LoginResponse, onResult: (Any?) -> Unit){
        val retrofit = ServiceBuilder.buildService(ApiInterface::class.java)
        retrofit.update(logResp).enqueue(
            object : Callback<Any> {
                override fun onResponse(call: Call<Any>, response: Response<Any>) {
                    val gson = Gson()
                    if (response.isSuccessful) {
                        val successfulResp: UserUpdateResponse = gson.fromJson(gson.toJson(response.body()), UserUpdateResponse::class.java)
                        onResult(successfulResp)
                    } else {
                        val errorBody = response.errorBody() ?: return
                        val type = object : TypeToken<ErrorResponse>() {}.type
                        val errorResponse: ErrorResponse? = gson.fromJson(errorBody.charStream(), type)
                        onResult(errorResponse)
                    }
                }
                override fun onFailure(call: Call<Any>, t: Throwable) {
                    onResult(null)
                }
            }
        )
    }

    fun friends(query: String, onResult: (Any?) -> Unit){
        val retrofit = ServiceBuilder.buildService(ApiInterface::class.java)
        retrofit.friends(query).enqueue(
            object : Callback<Any> {
                override fun onResponse(call: Call<Any>, response: Response<Any>) {
                    val gson = Gson()
                    if (response.isSuccessful) {
                        val successfulResp: FriendsResponse? = gson.fromJson(gson.toJson(response.body()), FriendsResponse::class.java)
                        onResult(successfulResp)
                    } else {
                        val errorBody = response.errorBody() ?: return
                        println(errorBody)
                        val type = object : TypeToken<ErrorResponse>() {}.type
                        val errorResponse: ErrorResponse? = gson.fromJson(errorBody.charStream(), type)
                        onResult(errorResponse)
                    }
                }
                override fun onFailure(call: Call<Any>, t: Throwable) {
                    onResult(null)
                }
            }
        )
    }
}
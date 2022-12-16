package com.example.locationservice.models

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class RegisterRequest (
    @SerializedName("login") var login: String?,
    @SerializedName("password") var password: String?,
    @SerializedName("full_name") var name: String?,
    @SerializedName("interests") var interests: String?,
    @SerializedName("update_freq") var update_freq: String?
) : Serializable

data class RegisterResponse (
    @SerializedName("login") var login: String?,
    @SerializedName("hashword") var hashword: String?,
    @SerializedName("full_name") var name: String?,
    @SerializedName("interests") var interests: String?
) : Serializable

data class LoginRequest (
    @SerializedName("login") var login: String?,
    @SerializedName("password") var password: String?,
) : Serializable

data class UserUpdateResponse (
    @SerializedName("success") var Success: String?,
) : Serializable

data class FriendsResponse (
    @SerializedName("users") var Users: List<LoginResponse>,
) : Serializable

data class LoginResponse (
    @SerializedName("uid") var uid: String?,
    @SerializedName("login") var login: String?,
    @SerializedName("hashword") var hashword: String?,
    @SerializedName("longitude") var longitude: Double?,
    @SerializedName("latitude") var latitude: Double?,
    @SerializedName("full_name") var name: String?,
    @SerializedName("interests") var interests: String?,
    @SerializedName("update_freq") var update_freq: String?,
    @SerializedName("online") var online: String?
) : Serializable

data class UserExistsResponse (
    @SerializedName("exists") var exists: Boolean,
) : Serializable

data class ErrorResponse (
    @SerializedName("status") var status: String?,
    @SerializedName("error") var error: String?
) : Serializable



package com.example.locationservice.api

import android.content.Context
import android.content.SharedPreferences
import com.example.locationservice.activities.LoginActivity
import com.google.gson.Gson
import com.google.gson.annotations.SerializedName
import okhttp3.Cookie
import okhttp3.CookieJar
import okhttp3.HttpUrl


class CookieMonster : CookieJar {

    private var cookies = Cookies(jar = mutableListOf<Cookie>())

    private var store: SharedPreferences = LoginActivity.applicationContext()
        .getSharedPreferences("storage", Context.MODE_PRIVATE)

    private val gson = Gson()

    override fun saveFromResponse(url: HttpUrl, cookieList: MutableList<Cookie>) {
        this.clearCookies()
        cookies.jar.addAll(cookieList)

        val storeEditor: SharedPreferences.Editor = store.edit()
        val json = gson.toJson(cookies)
        storeEditor.putString("cookies", json)
        storeEditor.apply()
    }

    override fun loadForRequest(url: HttpUrl): List<Cookie>  {
        val jsonCookies = store.getString("cookies", "{}")
        if (cookies.jar.isEmpty() && jsonCookies != "{}" && jsonCookies != null) {
            cookies = gson.fromJson(jsonCookies, Cookies::class.java)
        }
        if (cookies.jar == null) {
            cookies = Cookies(jar = mutableListOf<Cookie>())
        }
        return cookies.jar
    }

    private fun clearCookies() {
        cookies.jar.clear()
    }
}

data class Cookies(
    @SerializedName("jar") var jar: MutableList<Cookie>
)
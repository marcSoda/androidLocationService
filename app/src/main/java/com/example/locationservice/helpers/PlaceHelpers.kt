package com.example.locationservice.helpers

import android.content.Context
import android.location.Address
import android.location.Geocoder
import java.net.HttpURLConnection
import java.net.URL
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit
import kotlin.math.acos
import kotlin.math.cos
import kotlin.math.roundToInt
import kotlin.math.sin

class PlaceHelpers {
    fun getPlaces(query: String, lat: String, lng: String, rad: String, type: String): String {
        var result = ""
        try {
            var key = "AIzaSyBTg0DN9MR7ZwlFoId-mUcWtWlqvWwQxSs"
            var urlStr = "https://maps.googleapis.com/maps/api/place/textsearch/json?query=$query&location=$lat,$lng&radius=$rad&type=$type&key=$key"
            var url: URL = URL(urlStr)
            val connection: HttpURLConnection = url.openConnection() as HttpURLConnection
            connection.setRequestProperty("Content-Type", "application/json")
            connection.requestMethod = "GET"
            connection.doInput = true
            val br = connection.inputStream.bufferedReader()
            result = br.use { br.readText() }
            connection.disconnect()
        } catch (e: Exception) {
            println(e)
        }
        return result
    }

    fun getAddress(context: Context, latitude: Double, longitude: Double): String? {
        var strAdd = ""
        val geocoder = Geocoder(context, Locale.getDefault())
        try {
            val addresses: List<Address>? = geocoder.getFromLocation(latitude, longitude, 1)
            println(addresses)
            if (addresses != null) {
                val returnedAddress: Address = addresses[0]
                val strReturnedAddress = StringBuilder("")
                for (i in 0..returnedAddress.getMaxAddressLineIndex()) {
                    strReturnedAddress.append(returnedAddress.getAddressLine(i)).append("\n")
                }
                strAdd = strReturnedAddress.toString()
                println("Address: " + strAdd)
            } else {
                println("No address found")
                return("No address found")
            }
        } catch (e: Exception) {
            println("No address found")
            e.printStackTrace()
            return("No address found")
        }
        return strAdd.trim()
    }

    fun getTimeAgo(time: String): String {
        println(time)
        var ftime = time.replace('Z', ' ')
        ftime = time.replace('T', ' ')
        var ret = ""
        try {
            val format = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
            val past: Date = format.parse(ftime) as Date
            val now = Date()
            val seconds: Long = TimeUnit.MILLISECONDS.toSeconds(now.time - past.time)
            val minutes: Long = TimeUnit.MILLISECONDS.toMinutes(now.time - past.time)
            val hours: Long = TimeUnit.MILLISECONDS.toHours(now.time - past.time)
            val days: Long = TimeUnit.MILLISECONDS.toDays(now.time - past.time)
            if (seconds < 60) {
                ret = "$seconds seconds ago"
            } else if (minutes < 60) {
                ret = "$minutes minutes ago"
            } else if (hours < 24) {
                ret = "$hours hours ago"
            } else {
                ret = "$days days ago"
            }
        } catch (j: java.lang.Exception) {
            j.printStackTrace()
        }
        return ret
    }

    fun getDistance(lat1: Double, lon1: Double, lat2: Double, lon2: Double): Double {
        val theta = lon1 - lon2
        var dist = (sin(deg2rad(lat1))
                * sin(deg2rad(lat2))
                + (cos(deg2rad(lat1))
                * cos(deg2rad(lat2))
                * cos(deg2rad(theta))))
        dist = acos(dist)
        dist = rad2deg(dist)
        dist *= 60 * 1.1515
        return (dist * 100.0).roundToInt() / 100.0
    }

    private fun deg2rad(deg: Double): Double {
        return deg * Math.PI / 180.0
    }

    private fun rad2deg(rad: Double): Double {
        return rad * 180.0 / Math.PI
    }
}
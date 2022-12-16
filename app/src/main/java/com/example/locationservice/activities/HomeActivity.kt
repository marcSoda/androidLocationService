package com.example.locationservice.activities

import android.annotation.SuppressLint
import android.content.Intent
import android.location.Location
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.locationservice.api.ApiService
import com.example.locationservice.databinding.ActivityHomeBinding
import com.example.locationservice.helpers.PlaceHelpers
import com.example.locationservice.models.*
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import java.util.*


private lateinit var binding: ActivityHomeBinding
private lateinit var flc: FusedLocationProviderClient
private lateinit var user: LoginResponse

class HomeActivity : AppCompatActivity(), Runnable {
    private var show_info_flag = true
    private var loc_thread = Thread(this@HomeActivity)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (intent.hasExtra("login_response")) {
            val logResp = intent.getSerializableExtra("login_response") as? LoginResponse
            if (logResp != null) {
                user = logResp
            } else {
                Toast.makeText(this, "Fatal Error: No login data", Toast.LENGTH_SHORT).show()
            }
        } else {
            Toast.makeText(this, "Fatal Error: No login data", Toast.LENGTH_SHORT).show()
        }

        if (loc_thread.state == Thread.State.NEW) {
            loc_thread.start()
        }

        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnFindRestaurant.setOnClickListener {
            startActivity(
                Intent(this, PlacesActivity::class.java)
                    .putExtra("user", user)
                    .putExtra("type", "restaurant")
            )
        }

        binding.btnFindGas.setOnClickListener {
            startActivity(
                Intent(this, PlacesActivity::class.java)
                    .putExtra("user", user)
                    .putExtra("type", "gas_station")
            )
        }

        binding.btnFindFriends.setOnClickListener {
            startActivity(
                Intent(this, FriendsActivity::class.java)
                    .putExtra("user", user)
            )
        }
    }

    override fun onPause() {
        super.onPause()
        show_info_flag = false
    }

    override fun onRestart() {
        super.onRestart()
        show_info_flag = true
    }

    @SuppressLint("MissingPermission")
    override fun run() {
        flc = LocationServices.getFusedLocationProviderClient(this)
        while (true) {
            try {
                var sendUpdate = false
                flc.lastLocation
                    .addOnSuccessListener { location : Location? ->
                        if (location != null) {
                            val latitude = location.latitude
                            val longitude = location.longitude
                            if (latitude != user.latitude!! || longitude != user.longitude!!) {
                                user.longitude = longitude
                                user.latitude = latitude
                                val address = PlaceHelpers().getAddress(this, latitude, longitude)
                                println(address)
                                runOnUiThread {
                                    Toast.makeText(this, address, Toast.LENGTH_SHORT).show()
                                }

                                val api = ApiService()
                                api.update(user) {
                                    val errResp: ErrorResponse? = it as? ErrorResponse
                                    val updResp: UserUpdateResponse? = it as? UserUpdateResponse
                                    if (updResp != null) {
                                    } else if (errResp != null) {
                                        Toast.makeText(
                                            applicationContext,
                                            "Failed to update location",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    } else {
                                        Toast.makeText(
                                            applicationContext,
                                            "An unknown error has occurred",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    }
                                }
                            }
                        } else {
                            println("Location is null")
                            runOnUiThread {
                                Toast.makeText(this, "Location is null", Toast.LENGTH_SHORT).show()
                            }
                        }
                    }

                // Sleep
                val freq = user.update_freq?.toInt()
                if (freq != null) {
                    Thread.sleep((freq * 1000).toLong())
                } else {
                    Thread.sleep((1 * 1000).toLong())
                }
            } catch (e: InterruptedException) {
                Toast.makeText(this, "Fatal Error: Unable to get location", Toast.LENGTH_SHORT).show()
                e.printStackTrace()
            }
        }
    }
}
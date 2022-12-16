package com.example.locationservice.activities

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.locationservice.adapters.PlacesListAdapter
import com.example.locationservice.databinding.ActivityPlacesBinding
import com.example.locationservice.models.LoginResponse
import com.example.locationservice.helpers.PlaceHelpers
import com.example.locationservice.models.Place
import com.example.locationservice.models.Places
import com.example.locationservice.models.Result
import com.google.gson.GsonBuilder

private lateinit var user: LoginResponse
private lateinit var binding: ActivityPlacesBinding
private lateinit var type: String

class PlacesActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (intent.hasExtra("type")) {
            val maybeType = intent.getSerializableExtra("type") as? String
            if (maybeType != null) {
                type = maybeType
            } else {
                Toast.makeText(this, "Fatal Error: Type is null", Toast.LENGTH_SHORT).show()
            }
        } else {
            Toast.makeText(this, "Fatal Error: No type defined", Toast.LENGTH_SHORT).show()
        }

        if (intent.hasExtra("user")) {
            val maybeUser = intent.getSerializableExtra("user") as? LoginResponse
            if (maybeUser != null) {
                user = maybeUser
            } else {
                Toast.makeText(this, "Fatal Error: Login data is null", Toast.LENGTH_SHORT).show()
            }
        } else {
            Toast.makeText(this, "Fatal Error: No login data", Toast.LENGTH_SHORT).show()
        }

        binding = ActivityPlacesBinding.inflate(layoutInflater)
        setContentView(binding.root)

        var lvPlaces = binding.lvPlaces

        binding.btnSearch.setOnClickListener {
            val rawString = binding.etConstraints.text.toString().trim()
            val formattedString = rawString.replace(' ', '+')
            Thread {
                val res = PlaceHelpers().getPlaces(formattedString, user.latitude!!.toString(), user.longitude.toString(), "2000", type)
                val gson = GsonBuilder().create()
                val root = gson.fromJson(res, Places::class.java)
                var placesList: ArrayList<Place> = ArrayList<Place>()
                for (result: Result in root.results!!) {
                    val resLat = result.geometry.location.lat
                    val resLng = result.geometry.location.lng
                    val dist = PlaceHelpers().getDistance(user.latitude!!, user.longitude!!, resLat, resLng).toString()
                    val p = Place(result.name, result.formatted_address, "$dist miles away")
                    placesList.add(p)
                }
                runOnUiThread {
                    lvPlaces.adapter = PlacesListAdapter(this, placesList)
                }
            }.start()
        }
    }
}
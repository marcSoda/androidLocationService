package com.example.locationservice.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.locationservice.adapters.FriendsListAdapter
import com.example.locationservice.adapters.PlacesListAdapter
import com.example.locationservice.api.ApiService
import com.example.locationservice.databinding.ActivityFriendsBinding
import com.example.locationservice.helpers.PlaceHelpers
import com.example.locationservice.models.*
import com.google.gson.GsonBuilder

private lateinit var user: LoginResponse
private lateinit var binding: ActivityFriendsBinding

class FriendsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

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

        binding = ActivityFriendsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        var lvFriends = binding.lvFriends

        binding.btnSearch.setOnClickListener {
            var query = binding.etConstraints.text.toString().trim()
            if (query.trim().isEmpty()) {
                query = "null"
            }
            Thread {
                val api = ApiService()
                api.friends(query) {
                    val errResp: ErrorResponse? = it as? ErrorResponse
                    val frResp: FriendsResponse? = it as? FriendsResponse
                    var friendsList: ArrayList<Friend> = ArrayList<Friend>()
                    if (frResp != null && frResp.Users != null) {
                        for (f: LoginResponse in frResp.Users) {
                            val dist = PlaceHelpers().getDistance(user.latitude!!, user.longitude!!, f.latitude!!, f.longitude!!).toString()
                            val address = PlaceHelpers().getAddress(this, f.latitude!!, f.longitude!!)
                            val timeAgo = PlaceHelpers().getTimeAgo(f.online!!)
                            val f = Friend(f.name!!, f.interests!!, address!!, "$dist miles away", timeAgo)
                            friendsList.add(f)
                        }
                        runOnUiThread {
                            lvFriends.adapter = FriendsListAdapter(this, friendsList)
                        }
                    } else if (errResp != null) {
                        Toast.makeText(applicationContext,"There was an error", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(applicationContext,"No friends matching constraint", Toast.LENGTH_SHORT).show()
                    }
                }
            }.start()
        }
    }
}

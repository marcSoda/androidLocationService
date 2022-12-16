package com.example.locationservice.adapters

import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import com.example.locationservice.R
import com.example.locationservice.models.Friend


class FriendsListAdapter(private val context: Activity, private val friends: ArrayList<Friend>) : ArrayAdapter<Friend>(context, R.layout.friend_list_item, friends) {
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val inflater: LayoutInflater = LayoutInflater.from(context)
        val view: View = inflater.inflate(R.layout.friend_list_item, null)
        val name: TextView = view.findViewById(R.id.name)
        val interests: TextView = view.findViewById(R.id.interests)
        val address: TextView = view.findViewById(R.id.address)
        val distance: TextView = view.findViewById(R.id.distance)
        val lastSeen: TextView = view.findViewById(R.id.last_seen)

        name.text = friends[position].name
        interests.text = friends[position].interests
        address.text = friends[position].address
        distance.text = friends[position].distance
        lastSeen.text = friends[position].lastSeen

        return view
    }

}

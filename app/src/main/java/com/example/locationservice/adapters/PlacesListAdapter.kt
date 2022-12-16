package com.example.locationservice.adapters

import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import com.example.locationservice.R
import com.example.locationservice.models.Place


class PlacesListAdapter(private val context: Activity, private val places: ArrayList<Place>) : ArrayAdapter<Place>(context, R.layout.place_list_item, places) {
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val inflater: LayoutInflater = LayoutInflater.from(context)
        val view: View = inflater.inflate(R.layout.place_list_item, null)
        val name: TextView = view.findViewById(R.id.name)
        val address: TextView = view.findViewById(R.id.address)
        val distance: TextView = view.findViewById(R.id.distance)

        name.text = places[position].name
        address.text = places[position].address
        distance.text = places[position].distance

        return view
    }

}
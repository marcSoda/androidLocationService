package com.example.locationservice.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import com.example.locationservice.api.ApiService
import com.example.locationservice.databinding.ActivityInterestsBinding
import com.example.locationservice.models.ErrorResponse
import com.example.locationservice.models.LoginRequest
import com.example.locationservice.models.RegisterRequest
import com.example.locationservice.models.RegisterResponse
import java.util.*

class InterestsActivity : AppCompatActivity() {
    private lateinit var binding: ActivityInterestsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityInterestsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val regReq = intent.getSerializableExtra("register_request") as? RegisterRequest

        val cbBusiness = binding.cbInterestsBusiness
        val cbComputers = binding.cbInterestsComputers
        val cbEducation = binding.cbInterestsEducation
        val cbScience = binding.cbInterestsScience
        val btnSave = binding.btnSave
        val spfreqs = binding.spInterestsFreq
        val freqs = arrayOf("5 seconds", "10 seconds", "1 minute", "30 minutes")
        val adapter = ArrayAdapter<String>(
            this,
            android.R.layout.simple_spinner_dropdown_item,
            freqs
        )
        spfreqs.adapter = adapter

        btnSave.setOnClickListener {
            val api = ApiService()
            var ints: Vector<String> = Vector<String>()
            if (cbBusiness.isChecked) { ints.add(cbBusiness.text.toString()) }
            if (cbComputers.isChecked) { ints.add(cbComputers.text.toString()) }
            if (cbEducation.isChecked) { ints.add(cbEducation.text.toString()) }
            if (cbScience.isChecked) { ints.add(cbScience.text.toString()) }

            regReq?.interests = ints.toString()

            val freq = spfreqs.selectedItem.toString()
            if (freq == freqs[0]) {
                regReq?.update_freq = 5.toString()
            } else if (freq == freqs[1]) {
                regReq?.update_freq = 10.toString()
            } else if (freq == freqs[2]) {
                regReq?.update_freq = 60.toString()
            } else if (freq == freqs[3]) {
                regReq?.update_freq = 1800.toString()
            }

            if (regReq != null) {
                api.register(regReq) {
                    val errResp: ErrorResponse? = it as? ErrorResponse
                    val userResp: RegisterResponse? = it as? RegisterResponse
                    if (userResp != null) {
                        val logReq = LoginRequest(
                            login = regReq.login,
                            password = regReq.password,
                        )
                        startActivity(
                            Intent(this, LoginActivity::class.java).putExtra("login_request", logReq)
                        )
                    } else if (errResp != null) {
                        Toast.makeText(applicationContext,"Failed to register", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(applicationContext,"An unknown error has occurred", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }
}
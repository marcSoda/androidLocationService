package com.example.locationservice.activities

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.locationservice.api.ApiService
import com.example.locationservice.databinding.ActivityLoginBinding
import com.example.locationservice.models.ErrorResponse
import com.example.locationservice.models.LoginRequest
import com.example.locationservice.models.LoginResponse
import com.example.locationservice.models.RegisterRequest

class LoginActivity : AppCompatActivity() {
    init {
        instance = this
    }

    companion object {
        private var instance: LoginActivity? = null

        fun applicationContext() : Context {
            return instance!!.applicationContext
        }
    }

    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //starts Register activity without showing login. used because login MUST be loaded because cookieMonster uses shared preferences in login
        //register activity should NEVER be started directly
        if (intent.hasExtra("spawnRegister") && intent.getBooleanExtra("spawnRegister", true)) {
            startActivity(Intent(this, RegisterActivity::class.java))
            return
        } else if (intent.hasExtra("login_request")) {
            val logReq = intent.getSerializableExtra("login_request") as? LoginRequest
            if (logReq != null) {
                login(logReq)
                return
            }
        }
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.tvForgotPassword.setOnClickListener {
            Toast.makeText(applicationContext,"Sucks for you", Toast.LENGTH_SHORT).show()
        }

        binding.tvNoAccount.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }

        binding.btnLogin.setOnClickListener {
            val login = binding.etLoginLogin.text.toString().trim()
            val pass = binding.etLoginPassword.text.toString().trim()

            val logReq = LoginRequest(
                login = login,
                password = pass
            )
            login(logReq)
        }
    }

    private fun login(logReq: LoginRequest) {
        val api = ApiService()
        api.login(logReq) {
            val errResp: ErrorResponse? = it as? ErrorResponse
            val logResp: LoginResponse? = it as? LoginResponse
            if (logResp != null) {
                startActivity(
                    Intent(this, HomeActivity::class.java).putExtra("login_response", logResp)
                )
            } else if (errResp != null) {
                Toast.makeText(applicationContext,"Failed to login", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(applicationContext,"An unknown error has occurred", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
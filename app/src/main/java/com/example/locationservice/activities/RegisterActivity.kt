package com.example.locationservice.activities

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.locationservice.api.ApiService
import com.example.locationservice.databinding.ActivityRegisterBinding
import com.example.locationservice.models.ErrorResponse
import com.example.locationservice.models.RegisterRequest
import com.example.locationservice.models.UserExistsResponse

@SuppressLint("CheckResult")
class RegisterActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegisterBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.tvHaveAccount.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
        }

        binding.btnRegister.setOnClickListener {
            val email = binding.etRegisterEmail.text.toString().trim()
            val login = binding.etRegisterLogin.text.toString().trim()
            val name = binding.etRegisterName.text.toString().trim()
            val pass = binding.etRegisterPassword.text.toString().trim()
            val passConf = binding.etRegisterPasswordConfirm.text.toString().trim()

            if (email.isEmpty()) {
                binding.etRegisterLogin.error = "Email Required"
                return@setOnClickListener
            } else if (login.isEmpty()) {
                binding.etRegisterLogin.error = "Login Required"
                return@setOnClickListener
            } else if (name.isEmpty()) {
                binding.etRegisterName.error = "Name Required"
                return@setOnClickListener
            } else if (pass != passConf) {
                binding.etRegisterPasswordConfirm.error = "Passwords do not match"
                return@setOnClickListener
            }

            val api = ApiService()
            api.exists(login) {
                val errResp: ErrorResponse? = it as? ErrorResponse
                val existsResp: UserExistsResponse? = it as? UserExistsResponse
                if (existsResp != null) {
                    if (existsResp.exists) {
                        binding.etRegisterLogin.error = "Login Exists"
                    } else if (!existsResp.exists) {
                        val regReq = RegisterRequest(
                            login = login,
                            name = name,
                            password = pass,
                            interests = null,
                            update_freq = "10"
                        )
                        startActivity(
                            Intent(this, InterestsActivity::class.java).putExtra("register_request", regReq)
                        )
                    }
                } else if (errResp != null) {
                    Toast.makeText(applicationContext,"Failed to check if user exists", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(applicationContext,"An unknown error has occurred", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}
package com.example.enhanced_version

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import com.example.enhanced_version.databinding.ActivityRegisterBinding

class RegisterActivity : ComponentActivity() {
    private lateinit var binding: ActivityRegisterBinding
    private lateinit var dbHelper: DatabaseHelper
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val dbHelper = DatabaseHelper(this)
        binding.buttonRegister.setOnClickListener {
            val username = binding.editTextNewUsername.text.toString()
            val password = binding.editTextNewPassword.text.toString()
            if (username.isNotEmpty() && password.isNotEmpty()) {
                val isRegistered = dbHelper.registerUser(username, password)
                if (isRegistered) {
                    Toast.makeText(this, "User registered successfully", Toast.LENGTH_SHORT).show()
                    startActivity(Intent(this, MainActivity::class.java))
                } else {
                    Toast.makeText(this, "Registration failed", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show()
            }
        }
    }
}

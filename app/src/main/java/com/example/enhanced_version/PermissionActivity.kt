package com.example.enhanced_version

import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.enhanced_version.databinding.ActivityPermissionBinding
import android.Manifest
class PermissionActivity: ComponentActivity() {
    private val SMS_PERMISSION_CODE = 100
    private lateinit var binding: ActivityPermissionBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPermissionBinding.inflate(layoutInflater)
        setContentView(binding.root)
        requestSmsPermission()
        binding.buttonRequestPermissions.setOnClickListener {
            requestSmsPermission()
        }

        updatePermissionsStatus()
    }
    private fun requestSmsPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.SEND_SMS), SMS_PERMISSION_CODE)
        } else {
            Toast.makeText(this, "Permission already granted", Toast.LENGTH_SHORT).show()
            finish()
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == SMS_PERMISSION_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "SMS Permission GRANTED", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "SMS Permission DENIED", Toast.LENGTH_SHORT).show()
            }
            updatePermissionsStatus()
        }
    }

    private fun updatePermissionsStatus() {
        val permissionStatus = if (ContextCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS) == PackageManager.PERMISSION_GRANTED) {
            "Granted"
        } else {
            "Not Granted"
        }
        binding.textViewPermissionsStatus.text = "SMS Permission Status: $permissionStatus"
    }
}
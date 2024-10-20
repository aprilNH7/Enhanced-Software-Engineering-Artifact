package com.example.enhanced_version

import android.content.Intent
import android.os.Bundle
import android.widget.EditText
import android.widget.Toast
import androidx.activity.ComponentActivity
import com.example.enhanced_version.databinding.ActivityAddItemBinding

class AddItemActivity : ComponentActivity() {
    private lateinit var binding: ActivityAddItemBinding
    private lateinit var dbHelper: DatabaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddItemBinding.inflate(layoutInflater)
        setContentView(binding.root)

        dbHelper = DatabaseHelper(this)

        binding.buttonAddItem.setOnClickListener {
            val itemName = findViewById<EditText>(R.id.editTextItemName).text.toString()
            val itemQuantity = findViewById<EditText>(R.id.editTextItemQuantity).text.toString().toIntOrNull()
            val itemPrice = findViewById<EditText>(R.id.editTextItemPrice).text.toString().toDoubleOrNull()

            if (itemName.isNotEmpty() && itemQuantity != null && itemPrice != null) {
                val success = dbHelper.addItem(itemName, itemQuantity, itemPrice)
                if (success) {
                    Toast.makeText(this, "Item added successfully", Toast.LENGTH_SHORT).show()
                    val resultIntent = Intent()
                    setResult(RESULT_OK, resultIntent)
                    finish()
                } else {
                    Toast.makeText(this, "Failed to add item", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this, "Please fill all fields correctly", Toast.LENGTH_SHORT).show()
            }

        }
    }
}
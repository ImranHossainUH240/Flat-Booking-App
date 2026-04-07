package com.example.flatbookingapp

import android.os.Bundle
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.flatbookingapp.models.PropertyRequest
import com.example.flatbookingapp.RetrofitClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ListPropertyActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list_property)

        val btnSubmit = findViewById<Button>(R.id.btnNext)

        btnSubmit.setOnClickListener {
            // 1. Read the Basics
            val titleStr = findViewById<EditText>(R.id.etPropertyTitle)?.text?.toString() ?: ""
            val addressStr = findViewById<EditText>(R.id.etStreetAddress)?.text?.toString() ?: ""

            // 2. Read Pricing & Details
            val rentStr = findViewById<EditText>(R.id.etRent)?.text?.toString() ?: "0"
            val bedsStr = findViewById<EditText>(R.id.etBeds)?.text?.toString() ?: "0"
            val descStr = findViewById<EditText>(R.id.etDescription)?.text?.toString() ?: ""

            // 3. Read Amenities (Checkboxes)
            val isFlexible = findViewById<CheckBox>(R.id.cbFlexibleLease)?.isChecked ?: false
            val isVisaComp = findViewById<CheckBox>(R.id.cbVisa)?.isChecked ?: false
            val hasStudySpace = findViewById<CheckBox>(R.id.cbStudySpace)?.isChecked ?: false

            // Validation
            if (titleStr.isBlank() || addressStr.isBlank() || rentStr == "0") {
                Toast.makeText(this, "Please fill in Title, Address, and Rent", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            btnSubmit.isEnabled = false
            btnSubmit.text = "Uploading to Database..."

            // Format the description nicely
            val finalDescription = "Beds: $bedsStr | $descStr"

            // 4. Map everything to the Server Request
            val newListing = PropertyRequest(
                title = titleStr,
                location = addressStr,
                description = finalDescription,
                rent = rentStr.toIntOrNull() ?: 0,

                flexibleLease = isFlexible,
                visaCompatible = isVisaComp,
                studySpace = hasStudySpace,

                // Geographic & Commute Data (Hardcoded for now)
                // In Phase 3, we would use the Google Places API to convert the 'addressStr' into these numbers
                latitude = 51.5074,
                longitude = -0.1278,
                distanceToUniversity = 2.0,
                distanceToWork = 3.0,
                commuteTime = 20,
                transportCost = 3.5
            )

            // 5. Send the real data to the Ktor Server
            lifecycleScope.launch(Dispatchers.IO) {
                try {
                    RetrofitClient.apiService.postProperty(newListing)

                    withContext(Dispatchers.Main) {
                        Toast.makeText(this@ListPropertyActivity, "✅ Property Live on Server!", Toast.LENGTH_LONG).show()
                        finish()
                    }
                } catch (e: Exception) {
                    withContext(Dispatchers.Main) {
                        Toast.makeText(this@ListPropertyActivity, "Error: ${e.message}", Toast.LENGTH_LONG).show()
                        btnSubmit.isEnabled = true
                        btnSubmit.text = "Publish Live Listing"
                    }
                }
            }
        }
    }
}
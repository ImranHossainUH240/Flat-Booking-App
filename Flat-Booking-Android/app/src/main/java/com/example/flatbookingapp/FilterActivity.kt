package com.example.flatbookingapp

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.example.flatbookingapp.models.Property

class FilterActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_filter)

        // Get the full list passed from HomeFragment
        val propertyList = intent.getSerializableExtra("propertyList") as? ArrayList<Property> ?: arrayListOf()

        val etBudget = findViewById<EditText>(R.id.etBudget)
        val etUniDistance = findViewById<EditText>(R.id.etUniDistance)
        val etWorkDistance = findViewById<EditText>(R.id.etWorkDistance)
        val etCommuteTime = findViewById<EditText>(R.id.etCommuteTime)
        val etTransportCost = findViewById<EditText>(R.id.etTransportCost)

        val cbFlexibleLease = findViewById<CheckBox>(R.id.cbFlexibleLease)
        val cbVisaCompatible = findViewById<CheckBox>(R.id.cbVisaCompatible)
        val cbStudySpace = findViewById<CheckBox>(R.id.cbStudySpace)

        val btnApplyFilter = findViewById<Button>(R.id.btnApplyFilter)

        btnApplyFilter.setOnClickListener {
            // Read values (use max value if field is empty)
            val budget = etBudget.text.toString().toIntOrNull() ?: Int.MAX_VALUE
            val uniDistance = etUniDistance.text.toString().toDoubleOrNull() ?: Double.MAX_VALUE
            val workDistance = etWorkDistance.text.toString().toDoubleOrNull() ?: Double.MAX_VALUE
            val commuteTime = etCommuteTime.text.toString().toIntOrNull() ?: Int.MAX_VALUE
            val transportCost = etTransportCost.text.toString().toDoubleOrNull() ?: Double.MAX_VALUE

            val reqFlexible = cbFlexibleLease.isChecked
            val reqVisa = cbVisaCompatible.isChecked
            val reqStudy = cbStudySpace.isChecked

            // Filter the list based on user inputs
            val filteredList = propertyList.filter { property ->
                val matchBudget = property.rent <= budget
                val matchUniDist = property.distanceToUniversity <= uniDistance
                val matchWorkDist = property.distanceToWork <= workDistance
                val matchCommute = property.commuteTime <= commuteTime
                val matchCost = property.transportCost <= transportCost

                val matchFlexible = if (reqFlexible) property.flexibleLease else true
                val matchVisa = if (reqVisa) property.visaCompatible else true
                val matchStudy = if (reqStudy) property.studySpace else true

                // All conditions must be true
                matchBudget && matchUniDist && matchWorkDist && matchCommute && matchCost && matchFlexible && matchVisa && matchStudy
            }

            // Return the filtered list back to the previous screen
            val resultIntent = Intent()
            resultIntent.putExtra("filteredList", ArrayList(filteredList))
            setResult(RESULT_OK, resultIntent)
            finish()
        }
    }
}
package com.example.flatbookingapp.models

import java.io.Serializable

data class Property(
    val title: String,
    val description: String,
    val rent: Int,
    val location: String,
    val distanceToUniversity: Double,
    val distanceToWork: Double,
    val commuteTime: Int,
    val transportCost: Double,
    val flexibleLease: Boolean,
    val visaCompatible: Boolean,
    val studySpace: Boolean
): Serializable
// Added this new class for sending data TO the server
data class PropertyRequest(
    val title: String,
    val description: String,
    val rent: Int,
    val location: String,
    val latitude: Double,
    val longitude: Double,
    val distanceToUniversity: Double,
    val distanceToWork: Double,
    val commuteTime: Int,
    val transportCost: Double,
    val flexibleLease: Boolean,
    val visaCompatible: Boolean,
    val studySpace: Boolean
)
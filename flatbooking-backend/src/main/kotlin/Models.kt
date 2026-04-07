package com.example.flatbooking

import kotlinx.serialization.Serializable
import org.jetbrains.exposed.sql.Table

// 1. What the Android app SENDS to the server (No ID)
@Serializable
data class PropertyRequest(
    val title: String,
    val description: String,
    val rent: Int,
    val location: String,
    val latitude: Double,    // <-- New field for Map
    val longitude: Double,   // <-- New field for Map
    val distanceToUniversity: Double,
    val distanceToWork: Double,
    val commuteTime: Int,
    val transportCost: Double,
    val flexibleLease: Boolean,
    val visaCompatible: Boolean,
    val studySpace: Boolean
)

// 2. What the Server SENDS back to the Android app (Includes ID)
@Serializable
data class PropertyModel(
    val id: Int,
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

// 3. The actual Database Table schema
object PropertiesTable : Table() {
    val id = integer("id").autoIncrement()
    val title = varchar("title", 128)
    val description = varchar("description", 500)
    val rent = integer("rent")
    val location = varchar("location", 128)
    val latitude = double("latitude")   // <-- New column
    val longitude = double("longitude") // <-- New column
    val distanceToUniversity = double("distance_to_university")
    val distanceToWork = double("distance_to_work")
    val commuteTime = integer("commute_time")
    val transportCost = double("transport_cost")
    val flexibleLease = bool("flexible_lease")
    val visaCompatible = bool("visa_compatible")
    val studySpace = bool("study_space")

    override val primaryKey = PrimaryKey(id)
}
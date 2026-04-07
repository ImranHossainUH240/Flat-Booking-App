package com.example.flatbooking

import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction

object DatabaseFactory {
    fun init() {
        Database.connect("jdbc:h2:mem:test;DB_CLOSE_DELAY=-1", driver = "org.h2.Driver")

        transaction {
            SchemaUtils.create(PropertiesTable)

            // Dummy data updated with real coordinates in London
            PropertiesTable.insert {
                it[title] = "Studio Flat near University"
                it[description] = "Nice studio with furnished room and WiFi."
                it[rent] = 650
                it[location] = "London Zone 2"
                it[latitude] = 51.5074   // <-- London Lat
                it[longitude] = -0.1278  // <-- London Lng
                it[distanceToUniversity] = 2.0
                it[distanceToWork] = 4.0
                it[commuteTime] = 25
                it[transportCost] = 4.5
                it[flexibleLease] = true
                it[visaCompatible] = true
                it[studySpace] = true
            }
        }
    }

    suspend fun <T> dbQuery(block: suspend () -> T): T =
        newSuspendedTransaction { block() }

    // --- NEW FUNCTION: Safely insert a new property into the database ---
    suspend fun insertProperty(property: PropertyRequest): PropertyModel? {
        var generatedId = 0
        dbQuery {
            val insertStatement = PropertiesTable.insert {
                it[title] = property.title
                it[description] = property.description
                it[rent] = property.rent
                it[location] = property.location
                it[latitude] = property.latitude
                it[longitude] = property.longitude
                it[distanceToUniversity] = property.distanceToUniversity
                it[distanceToWork] = property.distanceToWork
                it[commuteTime] = property.commuteTime
                it[transportCost] = property.transportCost
                it[flexibleLease] = property.flexibleLease
                it[visaCompatible] = property.visaCompatible
                it[studySpace] = property.studySpace
            }
            generatedId = insertStatement[PropertiesTable.id]
        }

        // After inserting, fetch the newly created property to send back
        return dbQuery {
            PropertiesTable.selectAll().where { PropertiesTable.id eq generatedId }
                .map { rowToPropertyModel(it) }
                .singleOrNull()
        }
    }

    // Helper to keep code clean
    fun rowToPropertyModel(row: ResultRow): PropertyModel {
        return PropertyModel(
            id = row[PropertiesTable.id],
            title = row[PropertiesTable.title],
            description = row[PropertiesTable.description],
            rent = row[PropertiesTable.rent],
            location = row[PropertiesTable.location],
            latitude = row[PropertiesTable.latitude],
            longitude = row[PropertiesTable.longitude],
            distanceToUniversity = row[PropertiesTable.distanceToUniversity],
            distanceToWork = row[PropertiesTable.distanceToWork],
            commuteTime = row[PropertiesTable.commuteTime],
            transportCost = row[PropertiesTable.transportCost],
            flexibleLease = row[PropertiesTable.flexibleLease],
            visaCompatible = row[PropertiesTable.visaCompatible],
            studySpace = row[PropertiesTable.studySpace]
        )
    }
}
package com.example.flatbooking

import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction

object DatabaseFactory {

    fun init() {
        // 1. PASTE YOUR NEON JDBC URL HERE
        val jdbcUrl = "jdbc:postgresql://ep-royal-truth-a1mlm73d-pooler.ap-southeast-1.aws.neon.tech/neondb?user=neondb_owner&password=npg_gyKZ53ErJSka&sslmode=require&channelBinding=require"

        // 2. Connect to PostgreSQL instead of H2
        Database.connect(url = jdbcUrl, driver = "org.postgresql.Driver")

        transaction {
            // This creates the table in your cloud database if it doesn't exist yet
            SchemaUtils.create(PropertiesTable)

            // Optional: You can comment this dummy data out after the first run so it
            // doesn't keep adding the same "Studio Flat" every time you restart the server.
            val currentCount = PropertiesTable.selectAll().count()
            if (currentCount == 0L) {
                PropertiesTable.insert {
                    it[title] = "Studio Flat near University"
                    it[description] = "Nice studio with furnished room and WiFi."
                    it[rent] = 650
                    it[location] = "London Zone 2"
                    it[latitude] = 51.5074
                    it[longitude] = -0.1278
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
    } // <--- THIS WAS THE MISSING BRACE!

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
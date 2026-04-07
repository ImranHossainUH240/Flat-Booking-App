package com.example

import com.example.flatbooking.DatabaseFactory
import com.example.flatbooking.PropertiesTable
import com.example.flatbooking.PropertyRequest
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.jetbrains.exposed.sql.selectAll
import io.ktor.http.*
import io.ktor.server.request.*

fun main() {
    embeddedServer(Netty, port = 8080, host = "0.0.0.0", module = Application::module)
        .start(wait = true)
}

fun Application.module() {
    // 1. Enable JSON responses
    install(ContentNegotiation) {
        json()
    }

    // 2. Start the database
    DatabaseFactory.init()

    // 3. Define the API Routes
    routing {
        get("/") {
            call.respondText("StudentStay API is running!")
        }

        // The Student Feed Endpoint
        get("/properties") {
            val properties = DatabaseFactory.dbQuery {
                PropertiesTable.selectAll().map { DatabaseFactory.rowToPropertyModel(it) }
            }
            call.respond(properties)
        }

        // --- NEW: The Landlord Submission Endpoint ---
        post("/properties") {
            try {
                // 1. Receive the JSON from Android
                val propertyRequest = call.receive<PropertyRequest>()

                // 2. Save it to the database
                val newProperty = DatabaseFactory.insertProperty(propertyRequest)

                // 3. Tell the app it was successful (201 Created)
                if (newProperty != null) {
                    call.respond(HttpStatusCode.Created, newProperty)
                } else {
                    call.respond(HttpStatusCode.InternalServerError, "Database error")
                }
            } catch (e: Exception) {
                call.respond(HttpStatusCode.BadRequest, "Invalid JSON data received")
            }
        }
    }
}
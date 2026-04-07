package com.example.flatbookingapp

import com.example.flatbookingapp.models.Property
import com.example.flatbookingapp.models.PropertyRequest // <-- Import your new model
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface ApiService {
    // Student: Get all properties
    @GET("/properties")
    suspend fun getProperties(): List<Property>

    // Landlord: Post a new property
    @POST("/properties")
    suspend fun postProperty(@Body property: PropertyRequest): Property // Returns the newly created property
}

// 2. Build the Retrofit Client
object RetrofitClient {
    // 10.0.2.2 is the magic IP that lets the Android Emulator talk to your computer's localhost


    // private const val BASE_URL = "http://10.0.2.2:8080" // use this magic IP only when app runs on emulator
    private const val BASE_URL = "http://192.168.43.158:8080" // use device's IP only when app runs on real edge device

    val apiService: ApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService::class.java)
    }
}
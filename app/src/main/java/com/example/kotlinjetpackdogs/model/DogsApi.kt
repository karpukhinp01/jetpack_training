package com.example.kotlinjetpackdogs.model

import retrofit2.http.GET

interface DogsApi {
    @GET("DevTides/DogsApi/master/dogs.json")
    suspend fun getDogs(): List<DogBreed>
}
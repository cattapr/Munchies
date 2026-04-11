package com.example.myapplication.data.remote

import com.example.myapplication.data.model.RestaurantsResponse
import retrofit2.Response
import retrofit2.http.GET

interface RestaurantsApi {
    @GET("api/v1/restaurants")
    suspend fun getAllRestaurants(): Response<RestaurantsResponse>
}
package com.example.myapplication.data.remote

import com.example.myapplication.data.model.FilterResponse
import com.example.myapplication.data.model.RestaurantsResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface RestaurantsApi {
    @GET("api/v1/restaurants")
    suspend fun getAllRestaurants(): Response<RestaurantsResponse>

    @GET("api/v1/filter/{id}")
    suspend fun getFilter(@Path("id") id: String): Response<FilterResponse>
}
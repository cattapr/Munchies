package com.example.myapplication.data.remote

import com.example.myapplication.data.model.ErrorResponse
import com.example.myapplication.data.model.RestaurantsResponse
import com.google.gson.Gson
import javax.inject.Inject

interface IRestaurantsRemoteDataSource {
    suspend fun getAllRestaurants(): Result<RestaurantsResponse>
}


class RestaurantsRemoteDataSource @Inject constructor(
    private val api: RestaurantsApi,
    private val gson: Gson
) : IRestaurantsRemoteDataSource {

    override suspend fun getAllRestaurants(): Result<RestaurantsResponse> =
        try {
            val response = api.getAllRestaurants()
            if (response.isSuccessful) {
                response.body()?.let {
                    Result.success(it)
                } ?: Result.failure(Exception("Empty response body"))
            } else {
                val errorBody = response.errorBody()?.string()
                val errorResponse = try {
                    gson.fromJson(errorBody, ErrorResponse::class.java)
                } catch (e: Exception) {
                    null
                }
                Result.failure(Exception(errorResponse?.reason ?: "API error: ${response.code()}"))
            }
        } catch (error: Exception) {
            Result.failure(error)
        }
}
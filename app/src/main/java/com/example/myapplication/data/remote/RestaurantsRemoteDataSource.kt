package com.example.myapplication.data.remote

import com.example.myapplication.data.model.ErrorResponse
import com.example.myapplication.data.model.FilterResponse
import com.example.myapplication.data.model.OpenStatusResponse
import com.example.myapplication.data.model.RestaurantsResponse
import com.google.gson.Gson
import javax.inject.Inject

interface IRestaurantsRemoteDataSource {
    suspend fun getAllRestaurants(): Result<RestaurantsResponse>
    suspend fun getFilter(id: String): Result<FilterResponse>

    suspend fun getOpenStatus(id: String): Result<OpenStatusResponse>
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

    override suspend fun getFilter(id: String): Result<FilterResponse> =
        try {
            val response = api.getFilter(id)
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

    override suspend fun getOpenStatus(id: String): Result<OpenStatusResponse> =
        try {
            val response = api.getOpenStatus(id)
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
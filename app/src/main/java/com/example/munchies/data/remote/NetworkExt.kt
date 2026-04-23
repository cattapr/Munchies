package com.example.munchies.data.remote

import com.example.munchies.data.model.ErrorResponse
import com.google.gson.Gson
import retrofit2.Response

suspend fun <T> Response<T>.toResult(gson: Gson): Result<T> = try {
    if (isSuccessful) {
        body()?.let {
            Result.success(it)
        } ?: Result.failure(Exception("Empty response body"))
    } else {
        val errorResponse = try {
            gson.fromJson(errorBody()?.string(), ErrorResponse::class.java)
        } catch (e: Exception) {
            null
        }
        Result.failure(Exception(errorResponse?.reason ?: "API error: ${code()}"))
    }
} catch (e: Exception) {
    Result.failure(e)
}
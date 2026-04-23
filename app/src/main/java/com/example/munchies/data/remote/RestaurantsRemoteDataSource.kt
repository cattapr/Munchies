package com.example.munchies.data.remote

import com.example.munchies.data.model.FilterResponse
import com.example.munchies.data.model.OpenStatusResponse
import com.example.munchies.data.model.RestaurantsResponse
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
        api.getAllRestaurants().toResult(gson)

    override suspend fun getFilter(id: String): Result<FilterResponse> =
        api.getFilter(id).toResult(gson)

    override suspend fun getOpenStatus(id: String): Result<OpenStatusResponse> =
        api.getOpenStatus(id).toResult(gson)

}
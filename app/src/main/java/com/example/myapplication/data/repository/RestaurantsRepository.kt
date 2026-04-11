package com.example.myapplication.data.repository

import com.example.myapplication.data.mapper.toUiFilter
import com.example.myapplication.data.mapper.toUiOpenStatus
import com.example.myapplication.data.mapper.toUiRestaurant
import com.example.myapplication.data.remote.IRestaurantsRemoteDataSource
import com.example.myapplication.domain.model.Filter
import com.example.myapplication.domain.model.OpenStatus
import com.example.myapplication.domain.model.Restaurant
import com.example.myapplication.domain.repository.IRestaurantsRepository
import javax.inject.Inject

class RestaurantsRepository @Inject constructor(
    private val remoteDataSource: IRestaurantsRemoteDataSource
) : IRestaurantsRepository {

    override suspend fun getAllRestaurants(): Result<List<Restaurant>> {
        return remoteDataSource.getAllRestaurants().map { response ->
            response.toUiRestaurant()
        }
    }

    override suspend fun getFilter(id: String): Result<Filter> {
        return remoteDataSource.getFilter(id).map { it.toUiFilter() }
    }

    override suspend fun getOpenStatus(id: String): Result<OpenStatus> {
        return remoteDataSource.getOpenStatus(id).map { it.toUiOpenStatus() }
    }

}
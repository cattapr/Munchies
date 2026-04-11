package com.example.munchies.data.repository

import com.example.munchies.data.mapper.toUiFilter
import com.example.munchies.data.mapper.toUiOpenStatus
import com.example.munchies.data.mapper.toUiRestaurant
import com.example.munchies.data.remote.IRestaurantsRemoteDataSource
import com.example.munchies.domain.model.Filter
import com.example.munchies.domain.model.OpenStatus
import com.example.munchies.domain.model.Restaurant
import com.example.munchies.domain.repository.IRestaurantsRepository
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
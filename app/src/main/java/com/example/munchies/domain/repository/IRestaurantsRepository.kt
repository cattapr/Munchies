package com.example.munchies.domain.repository

import com.example.munchies.domain.model.Filter
import com.example.munchies.domain.model.OpenStatus
import com.example.munchies.domain.model.Restaurant


interface IRestaurantsRepository {
    suspend fun getAllRestaurants(): Result<List<Restaurant>>
    suspend fun getFilter(id: String): Result<Filter>
    suspend fun getOpenStatus(id: String): Result<OpenStatus>

}
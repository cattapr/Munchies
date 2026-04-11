package com.example.myapplication.domain.repository

import com.example.myapplication.domain.model.Filter
import com.example.myapplication.domain.model.OpenStatus
import com.example.myapplication.domain.model.Restaurant


interface IRestaurantsRepository {
    suspend fun getAllRestaurants(): Result<List<Restaurant>>
    suspend fun getFilter(id: String): Result<Filter>
    suspend fun getOpenStatus(id: String): Result<OpenStatus>

}
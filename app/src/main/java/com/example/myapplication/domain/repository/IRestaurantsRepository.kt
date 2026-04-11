package com.example.myapplication.domain.repository

import com.example.myapplication.domain.model.Restaurant


interface IRestaurantsRepository {
    suspend fun getAllRestaurants(): Result<List<Restaurant>>
}
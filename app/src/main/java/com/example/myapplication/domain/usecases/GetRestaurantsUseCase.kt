package com.example.myapplication.domain.usecases

import com.example.myapplication.domain.model.Restaurant
import com.example.myapplication.domain.repository.IRestaurantsRepository
import javax.inject.Inject


interface IGetRestaurantsUseCase {
    suspend operator fun invoke(): Result<List<Restaurant>>
}

class GetRestaurantsUseCase @Inject constructor(
    private val restaurantsRepository: IRestaurantsRepository
) : IGetRestaurantsUseCase {
    override suspend fun invoke(): Result<List<Restaurant>> =
        restaurantsRepository.getAllRestaurants()
}
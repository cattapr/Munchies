package com.example.munchies.domain.usecases

import com.example.munchies.domain.model.Restaurant
import com.example.munchies.domain.repository.IRestaurantsRepository
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
package com.example.myapplication.domain.usecases

import javax.inject.Inject

interface IRestaurantsUseCases {
    val getAllRestaurants: IGetRestaurantsUseCase
}

data class RestaurantsUseCases
@Inject
constructor(
    override val getAllRestaurants: IGetRestaurantsUseCase,
) : IRestaurantsUseCases
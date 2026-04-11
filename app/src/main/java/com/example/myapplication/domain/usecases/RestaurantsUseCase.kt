package com.example.myapplication.domain.usecases

import javax.inject.Inject

interface IRestaurantsUseCases {
    val getAllRestaurants: IGetRestaurantsUseCase
    val getFilter: IGetFilterUseCase
    val getOpenStatus: IGetOpenStatusUseCase
}

data class RestaurantsUseCases
@Inject
constructor(
    override val getAllRestaurants: IGetRestaurantsUseCase,
    override val getFilter: IGetFilterUseCase,
    override val getOpenStatus: IGetOpenStatusUseCase
) : IRestaurantsUseCases

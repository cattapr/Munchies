package com.example.myapplication.domain.di

import com.example.myapplication.domain.usecases.GetRestaurantsUseCase
import com.example.myapplication.domain.usecases.IGetRestaurantsUseCase
import com.example.myapplication.domain.usecases.IRestaurantsUseCases
import com.example.myapplication.domain.usecases.RestaurantsUseCases
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface ICoreDomainRestaurantModule {
    @Binds
    @Singleton
    fun bindRestaurantUseCases(restaurantsUseCase: RestaurantsUseCases): IRestaurantsUseCases


    @Binds
    @Singleton
    fun bindGetRestaurantsUseCase(
        getRestaurantsUseCase: GetRestaurantsUseCase
    ): IGetRestaurantsUseCase
}
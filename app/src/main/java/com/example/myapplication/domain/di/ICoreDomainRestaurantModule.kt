package com.example.myapplication.domain.di

import com.example.myapplication.domain.usecases.GetFilterUseCase
import com.example.myapplication.domain.usecases.GetOpenStatusUseCase
import com.example.myapplication.domain.usecases.GetRestaurantsUseCase
import com.example.myapplication.domain.usecases.IGetFilterUseCase
import com.example.myapplication.domain.usecases.IGetOpenStatusUseCase
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

    @Binds
    @Singleton
    fun bindGetFilterUseCase(
        getFilterUseCase: GetFilterUseCase
    ): IGetFilterUseCase

    @Binds
    @Singleton
    fun bindGetOpenStatusUseCase(
        getOpenStatusUseCase: GetOpenStatusUseCase
    ): IGetOpenStatusUseCase
}
package com.example.munchies.domain.di

import com.example.munchies.domain.usecases.GetFilterUseCase
import com.example.munchies.domain.usecases.GetOpenStatusUseCase
import com.example.munchies.domain.usecases.GetRestaurantsUseCase
import com.example.munchies.domain.usecases.IGetFilterUseCase
import com.example.munchies.domain.usecases.IGetOpenStatusUseCase
import com.example.munchies.domain.usecases.IGetRestaurantsUseCase
import com.example.munchies.domain.usecases.IRestaurantsUseCases
import com.example.munchies.domain.usecases.RestaurantsUseCases
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
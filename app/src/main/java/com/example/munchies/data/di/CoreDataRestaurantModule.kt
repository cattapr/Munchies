package com.example.munchies.data.di

import com.example.munchies.data.remote.IRestaurantsRemoteDataSource
import com.example.munchies.data.remote.RestaurantsRemoteDataSource
import com.example.munchies.data.repository.RestaurantsRepository
import com.example.munchies.domain.repository.IRestaurantsRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface ICoreDataRestaurantModule {
    @Binds
    @Singleton
    fun bindRestaurantRemoteDataSource(service: RestaurantsRemoteDataSource): IRestaurantsRemoteDataSource

    @Binds
    @Singleton
    fun bindRestaurantsRepository(repository: RestaurantsRepository): IRestaurantsRepository

}
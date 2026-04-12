package com.example.munchies.data.repository

import com.example.munchies.data.cache.CachedResult
import com.example.munchies.data.mapper.toUiFilter
import com.example.munchies.data.mapper.toUiOpenStatus
import com.example.munchies.data.mapper.toUiRestaurant
import com.example.munchies.data.remote.IRestaurantsRemoteDataSource
import com.example.munchies.di.ApplicationScope
import com.example.munchies.domain.model.Filter
import com.example.munchies.domain.model.OpenStatus
import com.example.munchies.domain.model.Restaurant
import com.example.munchies.domain.repository.IRestaurantsRepository
import kotlinx.coroutines.CoroutineScope
import javax.inject.Inject

class RestaurantsRepository @Inject constructor(
    private val remoteDataSource: IRestaurantsRemoteDataSource,
    @ApplicationScope private val coroutineScope: CoroutineScope
) : IRestaurantsRepository {

    private val restaurantsCache = CachedResult<Unit, List<Restaurant>>(
        coroutineScope = coroutineScope,
        fetcher = { _ ->
            remoteDataSource.getAllRestaurants().map { it.toUiRestaurant() }
        }
    )
    
    private val filterCache = mutableMapOf<String, CachedResult<String, Filter>>()

    private val openStatusCaches = mutableMapOf<String, CachedResult<String, OpenStatus>>()

    override suspend fun getAllRestaurants(ignoreCache: Boolean): Result<List<Restaurant>> =
        restaurantsCache.fetch(key = Unit, ignoreCache = ignoreCache)


    override suspend fun getFilter(id: String): Result<Filter> {
        val cache = filterCache.getOrPut(id) {
            CachedResult(
                coroutineScope = coroutineScope,
                fetcher = { filterId ->
                    remoteDataSource.getFilter(filterId).map { it.toUiFilter() }
                }
            )
        }
        return cache.fetch(key = id)
    }

    override suspend fun getOpenStatus(id: String, ignoreCache: Boolean): Result<OpenStatus> {
        val cache = openStatusCaches.getOrPut(id) {
            CachedResult(
                coroutineScope = coroutineScope,
                fetcher = { restaurantId ->
                    remoteDataSource.getOpenStatus(restaurantId).map { it.toUiOpenStatus() }
                }
            )
        }
        return cache.fetch(key = id, ignoreCache = ignoreCache)
    }
}
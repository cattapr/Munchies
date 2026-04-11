package com.example.myapplication.domain.usecases

import com.example.myapplication.domain.model.Filter
import com.example.myapplication.domain.repository.IRestaurantsRepository
import javax.inject.Inject


interface IGetFilterUseCase {
    suspend operator fun invoke(id: String): Result<Filter>
}

class GetFilterUseCase @Inject constructor(
    private val restaurantsRepository: IRestaurantsRepository
) : IGetFilterUseCase {
    override suspend fun invoke(id: String): Result<Filter> =
        restaurantsRepository.getFilter(id)
}
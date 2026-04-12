package com.example.munchies.domain.usecases

import com.example.munchies.domain.model.OpenStatus
import com.example.munchies.domain.repository.IRestaurantsRepository
import javax.inject.Inject


interface IGetOpenStatusUseCase {
    suspend operator fun invoke(id: String, ignoreCache: Boolean = false): Result<OpenStatus>
}

class GetOpenStatusUseCase @Inject constructor(
    private val restaurantsRepository: IRestaurantsRepository
) : IGetOpenStatusUseCase {
    override suspend fun invoke(id: String, ignoreCache: Boolean): Result<OpenStatus> =
        restaurantsRepository.getOpenStatus(id, ignoreCache)
}
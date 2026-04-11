package com.example.myapplication.domain.usecases

import com.example.myapplication.domain.model.OpenStatus
import com.example.myapplication.domain.repository.IRestaurantsRepository
import javax.inject.Inject


interface IGetOpenStatusUseCase {
    suspend operator fun invoke(id: String): Result<OpenStatus>
}

class GetOpenStatusUseCase @Inject constructor(
    private val restaurantsRepository: IRestaurantsRepository
) : IGetOpenStatusUseCase {
    override suspend fun invoke(id: String): Result<OpenStatus> =
        restaurantsRepository.getOpenStatus(id)
}
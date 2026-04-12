package com.example.munchies.data.cache

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

/**
 * In-memory cache for network responses.
 * Shares in-flight requests to avoid duplicate calls.
 * Caches last successful response.
 */
class CachedResult<K, T>(
    private val coroutineScope: CoroutineScope,
    private val fetcher: suspend (key: K) -> Result<T>
) {
    private val latestResponse = MutableStateFlow<Result<T>?>(null)
    private val latestSuccess = MutableStateFlow<T?>(null)
    private val mutex = Mutex()
    private var inFlight: Deferred<Result<T>>? = null
    private var lastKey: K? = null

    val state: StateFlow<Result<T>?> = latestResponse

    fun cachedValue(): T? = latestSuccess.value

    fun clearCache() {
        latestResponse.value = null
        latestSuccess.value = null
        lastKey = null
    }

    suspend fun fetch(key: K, ignoreCache: Boolean = false): Result<T> {
        if (!ignoreCache && key == lastKey) {
            latestSuccess.value?.let { return Result.success(it) }
        }

        return mutex.withLock {
            if (!ignoreCache && key == lastKey) {
                latestSuccess.value?.let { return Result.success(it) }
            }

            val currentInFlight = inFlight
            lastKey = key
            if (currentInFlight != null && !currentInFlight.isCompleted) {
                return@withLock currentInFlight
            }

            coroutineScope.async {
                fetcher(key).also { result ->
                    latestResponse.value = result
                    result.getOrNull()?.let { latestSuccess.value = it }
                }
            }.also { inFlight = it }
        }.await()
    }
}
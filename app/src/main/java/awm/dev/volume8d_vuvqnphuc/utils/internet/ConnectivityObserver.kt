package awm.dev.volume8d_vuvqnphuc.utils.internet

import kotlinx.coroutines.flow.Flow

interface ConnectivityObserver {
    enum class Status {
        Available, Losing, Lost, Unavailable
    }

    fun observe(): Flow<Status>
    suspend fun checkInternetAccess(): Status
}
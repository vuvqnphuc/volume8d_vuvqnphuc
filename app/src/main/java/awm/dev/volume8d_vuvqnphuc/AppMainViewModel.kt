package awm.dev.volume8d_vuvqnphuc

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import awm.dev.volume8d_vuvqnphuc.remote_config.RemoteConfigManager
import awm.dev.volume8d_vuvqnphuc.utils.internet.ConnectivityObserver
import awm.dev.volume8d_vuvqnphuc.utils.internet.NetworkConnectivityObserver
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AppMainViewModel @Inject constructor(
    private val connectivityObserver: NetworkConnectivityObserver,
    @dagger.hilt.android.qualifiers.ApplicationContext private val context: android.content.Context,
    val remoteConfigManager: RemoteConfigManager,
) : ViewModel() {

    init {
        fetchRemoteConfig()
    }

    private fun fetchRemoteConfig() {
        remoteConfigManager.fetchValues { success ->
            Log.d("AppViewModel", "RemoteConfig fetch completed: $success")
        }
    }

    private val _networkStatus = kotlinx.coroutines.flow.MutableStateFlow(
        if (awm.dev.volume8d_vuvqnphuc.utils.internet.isNetworkConnected(context))
            ConnectivityObserver.Status.Available
        else
            ConnectivityObserver.Status.Unavailable
    )
    val networkStatus = _networkStatus.asStateFlow()

    init {
        viewModelScope.launch {
            connectivityObserver.observe().collect {
                _networkStatus.value = it
            }
        }
    }

    fun refreshNetworkStatus() {
        viewModelScope.launch {
            val status = connectivityObserver.checkInternetAccess()
            _networkStatus.value = status
        }
    }

    // ADS
    fun isCheckADS(): Boolean = remoteConfigManager.isCheckADS()
    fun getBannerMusic(): String = remoteConfigManager.setBannerMusic()
    fun getBannerLanguage(): String = remoteConfigManager.setBannerLanguage()
    fun getBannerListMusic(): String = remoteConfigManager.setBannerListMusic()
    fun getBannerVolume(): String = remoteConfigManager.setBannerVolume()
    fun getBannerSetting(): String = remoteConfigManager.setBannerSetting()
    fun getInterListMusicMenu(): String = remoteConfigManager.setInterListMusicMenu()
    fun getInterDoneLanguage(): String = remoteConfigManager.setInterDoneLanguage()
}


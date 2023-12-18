package dev.eknath.aiconvo.ui.presentation.helpers

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext

sealed class NetworkState {
    data object Connected : NetworkState()
    data object Disconnected : NetworkState()
}

@Composable
fun networkStateProvider(): State<NetworkState> {
    val context = LocalContext.current
    val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

    var networkState by remember { mutableStateOf<NetworkState>(getInitialNetworkState(connectivityManager)) }

    DisposableEffect(Unit) {
        val callback = object : ConnectivityManager.NetworkCallback() {
            override fun onAvailable(network: android.net.Network) {
                networkState = NetworkState.Connected
            }

            override fun onLost(network: android.net.Network) {
                networkState = NetworkState.Disconnected
            }
        }

        val capabilities = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            NetworkCapabilities.NET_CAPABILITY_INTERNET
        } else {
            // For devices running on older versions
            @Suppress("DEPRECATION")
            null
        }

        connectivityManager.registerDefaultNetworkCallback(callback)

        onDispose {
            connectivityManager.unregisterNetworkCallback(callback)
        }
    }

    return remember(networkState) { mutableStateOf(networkState) }
}

private fun getInitialNetworkState(connectivityManager: ConnectivityManager): NetworkState {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        val networkCapabilities = connectivityManager.activeNetwork ?: return NetworkState.Disconnected
        val capabilities = connectivityManager.getNetworkCapabilities(networkCapabilities) ?: return NetworkState.Disconnected

        if (capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)) {
            NetworkState.Connected
        } else {
            NetworkState.Disconnected
        }
    } else {
        @Suppress("DEPRECATION")
        val networkInfo = connectivityManager.activeNetworkInfo ?: return NetworkState.Disconnected

        @Suppress("DEPRECATION")
        if (networkInfo.isConnected) {
            NetworkState.Connected
        } else {
            NetworkState.Disconnected
        }
    }
}

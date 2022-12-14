package com.brainer.itmmunity.utility

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo
import kotlin.properties.Delegates


class NetworkConnectCheck {
    fun isConnectNetwork(context: Context): Boolean {
        var isConnected by Delegates.notNull<Boolean>()

        val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork: NetworkInfo? = cm.activeNetworkInfo
        isConnected = activeNetwork?.isConnectedOrConnecting == true

        return isConnected
    }
}

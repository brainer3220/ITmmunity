package com.brainer.itmmunity.presentation.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import com.brainer.itmmunity.utility.NetworkConnectCheck
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

/**
 * @author brainer
 * @param context Context
 */
class BackGroundViewModel(application: Application) : AndroidViewModel(application) {
    private var _context = application.applicationContext

    private var _isConnect = MutableStateFlow<Boolean>(true)
    val isConnect = _isConnect.asStateFlow()

    init {
        getConnectState()
    }

    /**
     * This is internet connection check.
     * Checking the internet connect every 3 sec.
     * @author bariner
     * @return NO RETURN
     */
    private fun getConnectState() {
        CoroutineScope(Dispatchers.Default).launch {
            while (true) {
                delay(3000L)
                kotlin.runCatching {
                    NetworkConnectCheck().isConnectNetwork(_context.applicationContext)
                }.onSuccess {
                    if (isConnect.value != it) {
                        CoroutineScope(Dispatchers.Main).launch {
                            _isConnect.value = it
                        }
                        CoroutineScope(Dispatchers.Default).launch {
                            Log.d("isConnection", it.toString())
                        }
                    }
                }
            }
        }
    }
}

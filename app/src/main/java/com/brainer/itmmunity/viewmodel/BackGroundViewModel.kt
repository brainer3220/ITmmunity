package com.brainer.itmmunity.viewmodel

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.brainer.itmmunity.Utility.NetworkConnectCheck
import kotlinx.coroutines.*

/**
 * @author brainer
 * @param context Context
 */
class BackGroundViewModel(context: Context) : ViewModel() {
    private var _context = MutableLiveData(context)

    private var _isConnect = MutableLiveData<Boolean>(true)
    val isConnect: LiveData<Boolean> = _isConnect

    init {
        getConnectState()
    }

    /**
     * This is internet connection check.
     * Checking the internet connect every 3 sec.
     * @author bariner
     * @return NO RETURN
     */
    @OptIn(DelicateCoroutinesApi::class)
    fun getConnectState() {
        CoroutineScope(Dispatchers.Default).launch {
            while (true) {
                delay(3000L)
                kotlin.runCatching {
                    NetworkConnectCheck().isConnectNetwork(_context.value!!)
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

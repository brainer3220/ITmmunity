package com.brainer.itmmunity.ViewModel

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.brainer.itmmunity.Utility.NetworkConnectCheck
import kotlinx.coroutines.*

class NetworkCheckViewModel(context: Context) : ViewModel() {
    private var _context = MutableLiveData(context)

    private var _isConnect = MutableLiveData<Boolean>(true)
    val isConnect: LiveData<Boolean> = _isConnect

    init {
        getConnectState()
    }

    @OptIn(DelicateCoroutinesApi::class)
    fun getConnectState() {
        GlobalScope.launch {
            CoroutineScope(Dispatchers.Default).launch {
                while(true) {
                    delay(3000L)
                    kotlin.runCatching {
                        NetworkConnectCheck().isConnectNetwork(_context.value!!)
                    }.onSuccess {
                        if (isConnect.value != it) {
                            CoroutineScope(Dispatchers.Main).launch {
                                _isConnect.value = it
                            }
//                            CoroutineScope(Dispatchers.Default).launch {
                            Log.d("isConnection", it.toString())
//                            }
                        }
                    }
                }
            }
        }
    }
}

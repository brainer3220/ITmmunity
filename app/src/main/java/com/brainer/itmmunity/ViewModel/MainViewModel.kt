package com.brainer.itmmunity.ViewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.brainer.itmmunity.Croll.Croll
import com.brainer.itmmunity.Croll.KGNewsContent
import com.brainer.itmmunity.Croll.MeecoNews
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainViewModel : ViewModel() {
    private var _unifiedList = MutableLiveData(listOf<Croll.Content>())
    val unifiedList: LiveData<List<Croll.Content>> = _unifiedList

    private var _underKgNextPage = MutableLiveData(0)
    val underKgNextPage: LiveData<Int> = _underKgNextPage

    private var _meecoNextPage = MutableLiveData(0)
    val meecoNextPage: LiveData<Int> = _meecoNextPage


    init {
        getRefresh()
    }

    fun getRefresh() {
        viewModelScope.launch {
            CoroutineScope(Dispatchers.IO).launch {
                kotlin.runCatching {
                    KGNewsContent().returnData()
                }.onSuccess {
                    CoroutineScope(Dispatchers.Main).launch {
                        _unifiedList.value = _unifiedList.value!! + it
                        _unifiedList.value = _unifiedList.value?.toSet()?.toList()
                        _underKgNextPage.value = 1
                    }
                }
                kotlin.runCatching {
                    MeecoNews().returnData()
                }.onSuccess {
                    CoroutineScope(Dispatchers.Main).launch {
                        _unifiedList.value = _unifiedList.value?.plus(it.slice(3 until it.size))
                        _unifiedList.value = _unifiedList.value?.toSet()?.toList()
                        _meecoNextPage.value = 1
                    }
                }
            }
        }
    }

    fun addData() {
        viewModelScope.launch {
            CoroutineScope(Dispatchers.IO).launch {
                kotlin.runCatching {
                    KGNewsContent().returnData(_underKgNextPage.value!! + 1)
                }.onSuccess {
                    CoroutineScope(Dispatchers.Main).launch {
                        _unifiedList.value = _unifiedList.value!! + it
                        _unifiedList.value = _unifiedList.value?.toSet()?.toList()
                        _underKgNextPage.value = _underKgNextPage.value?.plus(1)
                    }
                }
                kotlin.runCatching {
                    MeecoNews().returnData(_meecoNextPage.value!! + 1)
                }.onSuccess {
                    CoroutineScope(Dispatchers.Main).launch {
                        _unifiedList.value = _unifiedList.value?.plus(it.slice(3 until it.size))
                        _unifiedList.value = _unifiedList.value?.toSet()?.toList()
                        _meecoNextPage.value = _meecoNextPage.value?.plus(1)
                    }
                }
            }
        }
    }
}

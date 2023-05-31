package com.brainer.itmmunity.presentation.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.brainer.itmmunity.data.Croll.Croll
import com.brainer.itmmunity.data.Croll.KGNewsContent
import com.brainer.itmmunity.data.Croll.MeecoNews
import com.google.firebase.ktx.Firebase
import com.google.firebase.remoteconfig.ktx.remoteConfig
import com.google.firebase.remoteconfig.ktx.remoteConfigSettings
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

const val CONFIG_STR = "Config"

enum class LoadState {
    LOADING,
    LOADED,
    ERROR,
}

class MainViewModel(application: Application) : AndroidViewModel(application) {
    private val _loadState = MutableStateFlow(LoadState.LOADED)
    val loadState = _loadState.asStateFlow()

    private var _unifiedList = MutableStateFlow(listOf<Croll.Content>())
    val unifiedList = _unifiedList.asStateFlow()

    private var _underKgNextPage = MutableStateFlow(0)
//    val underKgNextPage: LiveData<Int> = _underKgNextPage

    private var _meecoNextPage = MutableStateFlow(0)
//    val meecoNextPage: LiveData<Int> = _meecoNextPage

    private var _isTabletUi = MutableStateFlow(false)
    val isTabletUi = _isTabletUi.asStateFlow()

    private var _titleString = MutableStateFlow("ITmmunity")
    val titleString = _titleString.asStateFlow()

    var meecoNewsSliceValue = 0

    init {
        getRefresh()
        getMeecoSliceValue()
    }

    private fun getMeecoSliceValue() {
        val remoteConfig = Firebase.remoteConfig
        val configSettings = remoteConfigSettings {
            minimumFetchIntervalInSeconds = 3600
        }
        remoteConfig.setConfigSettingsAsync(configSettings)
        remoteConfig.fetch().addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val updated = task.result
                meecoNewsSliceValue = remoteConfig.getLong("meecoNewsSlice").toInt()
                Log.d(CONFIG_STR, "Config params updated: $updated")
                Log.d(CONFIG_STR, "Fetch and activate succeeded")
            } else {
                Log.d(CONFIG_STR, "Fetch failed")
            }
        }
    }

    fun getRefresh() {
        viewModelScope.launch(Dispatchers.IO) {
            _loadState.value = LoadState.LOADING
            _unifiedList.value = listOf<Croll.Content>()
            kotlin.runCatching {
                KGNewsContent().returnData()
            }.onSuccess {
                _unifiedList.value = _unifiedList.value + it
                _unifiedList.value = _unifiedList.value.toSet().toList()
                _underKgNextPage.value = 1
                _loadState.value = LoadState.LOADED
            }.onFailure {
                _loadState.value = LoadState.ERROR
            }
        }.onJoin

        viewModelScope.launch(Dispatchers.IO) {
            kotlin.runCatching {
                MeecoNews().returnData()
            }.onSuccess {
                _unifiedList.value =
                    _unifiedList.value.plus(it.slice(meecoNewsSliceValue until it.size))
                _unifiedList.value = _unifiedList.value.toSet().toList()
                _meecoNextPage.value = 1
            }
        }.onJoin
    }

    fun addData() {
        viewModelScope.launch(Dispatchers.IO) {
            kotlin.runCatching {
                KGNewsContent().returnData(_underKgNextPage.value + 1)
            }.onSuccess {
                _unifiedList.value = _unifiedList.value.union(it).toList()
                _underKgNextPage.value = _underKgNextPage.value.plus(1)
            }
        }.onJoin

        viewModelScope.launch(Dispatchers.IO) {
            kotlin.runCatching {
                MeecoNews().returnData(_meecoNextPage.value + 1)
            }.onSuccess {
                _unifiedList.value =
                    _unifiedList.value.union(it.slice(meecoNewsSliceValue until it.size))
                        .toList()
                _meecoNextPage.value = _meecoNextPage.value.plus(1)
            }
        }.onJoin
    }

    private var _aNews = MutableStateFlow<Croll.Content?>(null)
    val aNews: MutableStateFlow<Croll.Content?> = _aNews

    fun changeAnews(news: Croll.Content?) {
        viewModelScope.launch {
            _aNews.value = news
        }
    }

    /**
     * This function determines whether the View generates a Tablet UI.
     * @author brainer
     * @param boool If Tablet UI, true, else false.
     * @return NO RETURN
     */
    fun changeTabletUi(bool: Boolean) {
        viewModelScope.launch {
            _isTabletUi.value = bool
        }
    }

    /**
     * AppBar Title value
     * @author brainer
     * @param target String type
     * @return NO RETURN
     */
    fun changeTitle(target: String) {
        CoroutineScope(Dispatchers.Main).launch {
            _titleString.value = target
        }
    }
}

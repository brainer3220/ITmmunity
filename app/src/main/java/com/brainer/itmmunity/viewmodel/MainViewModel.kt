package com.brainer.itmmunity.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.brainer.itmmunity.Croll.Croll
import com.brainer.itmmunity.Croll.KGNewsContent
import com.brainer.itmmunity.Croll.MeecoNews
import com.google.firebase.ktx.Firebase
import com.google.firebase.remoteconfig.ktx.remoteConfig
import com.google.firebase.remoteconfig.ktx.remoteConfigSettings
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

const val CONFIG_STR = "Config"

class MainViewModel : ViewModel() {
    private var _unifiedList = MutableLiveData(listOf<Croll.Content>())
    val unifiedList: LiveData<List<Croll.Content>> = _unifiedList

    private var _underKgNextPage = MutableLiveData(0)
//    val underKgNextPage: LiveData<Int> = _underKgNextPage

    private var _meecoNextPage = MutableLiveData(0)
//    val meecoNextPage: LiveData<Int> = _meecoNextPage

    private var _isTabletUi = MutableLiveData(false)
    val isTabletUi : LiveData<Boolean> = _isTabletUi

    private var _titleString = MutableLiveData("ITmmunity")
    val titleString : LiveData<String> = _titleString

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
        remoteConfig.fetch()
            .addOnCompleteListener { task ->
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
        viewModelScope.launch {
            CoroutineScope(Dispatchers.IO).launch {
                CoroutineScope(Dispatchers.Main).launch {
                    _unifiedList.value = listOf<Croll.Content>()
                }
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
                        _unifiedList.value =
                            _unifiedList.value?.plus(it.slice(meecoNewsSliceValue until it.size))
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
                        _unifiedList.value = _unifiedList.value!!.union(it).toList()
                        _underKgNextPage.value = _underKgNextPage.value?.plus(1)
                    }
                }
                kotlin.runCatching {
                    MeecoNews().returnData(_meecoNextPage.value!! + 1)
                }.onSuccess {
                    CoroutineScope(Dispatchers.Main).launch {
                        _unifiedList.value =
                            _unifiedList.value?.union(it.slice(meecoNewsSliceValue until it.size))?.toList()
                        _meecoNextPage.value = _meecoNextPage.value?.plus(1)
                    }
                }
            }
        }
    }

    private var _aNews = MutableLiveData<Croll.Content>(null)
    val aNews: LiveData<Croll.Content> = _aNews

    fun changeAnews(news: Croll.Content?) {
        CoroutineScope(Dispatchers.Main).launch {
            _aNews.value = news
            if (news == null) {
                changeHtml(null)
            }
        }
    }

    /**
     * ContentView is referenced contentHtml for drawing View
     * @author brainer
     * @param html String type. html HTML String
     * @return NO RETURN
     */
    fun changeHtml(html: String?) {
        CoroutineScope(Dispatchers.Main).launch {
            _contentHtml.value = html
        }
    }

    private var _contentHtml = MutableLiveData("")
    val contentHtml: LiveData<String> = _contentHtml

    fun getHtml() {
        viewModelScope.launch {
            CoroutineScope(Dispatchers.IO).launch {
                kotlin.runCatching {
                    aNews.value!!.htmlToMarkdown()
                }.onSuccess {
                    val contentHtmlTmp = it!!.slice(2 until it.length - 1)
                    changeHtml(contentHtmlTmp)
                    Log.d("getHtmlViewModel", contentHtmlTmp)
                }.onFailure {
                    Log.w("getHtmlViewModel", "Failed")
                }
            }
        }
    }

    /**
     * This function determines whether the View generates a Tablet UI.
     * @author brainer
     * @param boool If Tablet UI, true, else false.
     * @return NO RETURN
     */
    fun changeTabletUi(bool: Boolean) {
        CoroutineScope(Dispatchers.Main).launch {
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
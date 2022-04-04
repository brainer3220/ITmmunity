package com.brainer.itmmunity.ViewModel

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

class MainViewModel : ViewModel() {
    private var _unifiedList = MutableLiveData(listOf<Croll.Content>())
    val unifiedList: LiveData<List<Croll.Content>> = _unifiedList

    private var _underKgNextPage = MutableLiveData(0)
    val underKgNextPage: LiveData<Int> = _underKgNextPage

    private var _meecoNextPage = MutableLiveData(0)
    val meecoNextPage: LiveData<Int> = _meecoNextPage

    private var _clicked = MutableLiveData(false)
    val clicked: LiveData<Boolean> = _clicked

    var meecoNewsSliceValue = 0

//    private var _appWidth by remember(mutableStateOf(1.Dp))

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
                    Log.d("Config", "Config params updated: $updated")
                    Log.d("Config", "Fetch and activate succeeded")
                } else {
                    Log.d("Config", "Fetch failed")
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
                        _unifiedList.value = _unifiedList.value!! + it
                        _unifiedList.value = _unifiedList.value?.toSet()?.toList()
                        _underKgNextPage.value = _underKgNextPage.value?.plus(1)
                    }
                }
                kotlin.runCatching {
                    MeecoNews().returnData(_meecoNextPage.value!! + 1)
                }.onSuccess {
                    CoroutineScope(Dispatchers.Main).launch {
                        _unifiedList.value =
                            _unifiedList.value?.plus(it.slice(meecoNewsSliceValue until it.size))
                        _unifiedList.value = _unifiedList.value?.toSet()?.toList()
                        _meecoNextPage.value = _meecoNextPage.value?.plus(1)
                    }
                }
            }
        }
    }

    fun changeClickedValue() {
        CoroutineScope(Dispatchers.Main).launch {
            _clicked.value = !_clicked.value!!
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

    private fun changeHtml(html: String?) {
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
                    aNews.value!!.htmlToMarkdown(
                        aNews.value!!.returnContent(aNews.value!!).toString()
                    )!!.replace("\t", "").replace("    ", "")
                }.onSuccess {
                    val contentHtmlTmp = it.slice(2 until it.length - 1)
                    changeHtml(contentHtmlTmp)
                    Log.d("getHtmlViewModel", contentHtmlTmp)
                }
                    .onFailure {
                        Log.d("getHtmlViewModel", "Failed")
                    }
            }
        }
    }
}

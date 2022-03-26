package com.brainer.itmmunity.ViewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.brainer.itmmunity.Croll.Croll
import com.brainer.itmmunity.FIT_IMAGE_SCRIPT
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class ContentViewModel: ViewModel() {
    private var _contentHtml = MutableLiveData("")
    val contentHtml: LiveData<String> = _contentHtml

    fun getHtml(news: Croll.Content) {
        viewModelScope.launch {
            CoroutineScope(Dispatchers.IO).launch {
                kotlin.runCatching {
                    news.let { news.returnContent(it).toString() } + FIT_IMAGE_SCRIPT
                }.onSuccess {
                    CoroutineScope(Dispatchers.Main).launch {
                        _contentHtml.value = it
                    }
                    Log.d("getHtmlViewModel", it)
                }
            }
        }
    }
}

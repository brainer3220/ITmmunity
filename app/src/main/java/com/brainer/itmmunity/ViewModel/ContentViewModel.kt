package com.brainer.itmmunity.ViewModel

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.brainer.itmmunity.Croll.Croll
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ContentViewModel : ViewModel() {
    private var _contentHtml = MutableLiveData("")
    val contentHtml: LiveData<String> = _contentHtml

    fun getHtml(news: Croll.Content) {
        viewModelScope.launch {
            CoroutineScope(Dispatchers.IO).launch {
                kotlin.runCatching {
                    news.htmlToMarkdown(news.returnContent(news).toString())!!.replace("\t", "").replace("    ", "")
                }.onSuccess {
                    val contentHtmlTmp = it.slice(2 until it!!.length-1)
                    CoroutineScope(Dispatchers.Main).launch {
                        _contentHtml.value = contentHtmlTmp
                    }
                    if (contentHtmlTmp != null) {
                        Log.d("getHtmlViewModel", contentHtmlTmp)
                    }
                }
            }
        }
    }
}

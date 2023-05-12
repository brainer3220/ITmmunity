package com.brainer.itmmunity.presentation.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.brainer.itmmunity.data.Croll.Croll
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class ContentViewModel(application: Application) : AndroidViewModel(application) {
    private val _aNews = MutableStateFlow(
        Croll.Content(
            title = "",
            image = null,
            hit = 0,
            url = "",
            numComment = null,
        ),
    )

    private var _contentHtml = MutableStateFlow<String>("")
    val contentHtml: MutableStateFlow<String> = _contentHtml

    fun setContent(content: Croll.Content?) {
        viewModelScope.launch {
            if (content != null) {
                _aNews.value = content
            }
            getHtml()
            Log.d("ContentViewModel", "setContent: ${_aNews.value}")
        }
    }

    /**
     * ContentView is referenced contentHtml for drawing View
     * @author brainer
     * @param html String type. html HTML String
     * @return NO RETURN
     */
    private fun changeHtml(html: String?) {
        viewModelScope.launch {
            if (html != null) {
                _contentHtml.value = html
            }
        }
    }

    fun getHtml() {
        viewModelScope.launch(Dispatchers.IO) {
            kotlin.runCatching {
                _aNews.value.htmlToMarkdown()
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

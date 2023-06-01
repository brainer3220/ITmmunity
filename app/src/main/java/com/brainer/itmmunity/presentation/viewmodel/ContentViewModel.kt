package com.brainer.itmmunity.presentation.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.brainer.itmmunity.domain.model.ContentModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ContentViewModel(application: Application) : AndroidViewModel(application) {
    private val _loadState = MutableStateFlow(LoadState.LOADED)
    val loadState = _loadState.asStateFlow()

    private val _aNews = MutableStateFlow(
        ContentModel(
            title = "",
            image = null,
            hit = 0,
            url = "",
            numComment = null,
        ),
    )

    private var _contentHtml = MutableStateFlow<String>("")
    val contentHtml: MutableStateFlow<String> = _contentHtml

    fun setContent(content: ContentModel?) {
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
            _loadState.value = LoadState.LOADING
            kotlin.runCatching {
                _aNews.value.htmlToMarkdown()
            }.onSuccess {
                val contentHtmlTmp = it!!.slice(2 until it.length - 1)
                changeHtml(contentHtmlTmp)
                _loadState.value = LoadState.LOADED
//                Log.d("getHtmlViewModel", contentHtmlTmp)
            }.onFailure {
                _loadState.value = LoadState.ERROR
                Log.e("getHtml", "Failed")
                return@launch
            }
        }
    }
}

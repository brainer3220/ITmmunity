package com.brainer.itmmunity.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.brainer.itmmunity.Croll.Croll
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class ContentViewModel : ViewModel() {
    private val _aNews = MutableStateFlow(
        Croll.Content(
            title = "",
            image = null,
            hit = 0,
            url = "",
            numComment = null
        )
    )

    private var _contentHtml = MutableStateFlow<String>("")
    val contentHtml: MutableStateFlow<String> = _contentHtml

    fun setContent(content: Croll.Content?) {
        CoroutineScope(Dispatchers.Main).launch {
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
        CoroutineScope(Dispatchers.Main).launch {
            if (html != null) {
                _contentHtml.value = html
            }
        }
    }

    fun getHtml() {
        viewModelScope.launch {
            CoroutineScope(Dispatchers.IO).launch {
                kotlin.runCatching {
                    _aNews.value.htmlToMarkdown()
                }.onSuccess {
                    val contentHtmlTmp = it!!.slice(2 until it.length -1)
                    changeHtml(contentHtmlTmp)
                    Log.d("getHtmlViewModel", contentHtmlTmp)
                }.onFailure {
                    Log.w("getHtmlViewModel", "Failed")
                }
            }
        }
    }
}

package com.brainer.itmmunity.presentation.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.brainer.itmmunity.domain.data.MeecoDataSource
import com.brainer.itmmunity.domain.data.UnderkgDataSource
import com.brainer.itmmunity.domain.model.ContentModel
import com.brainer.itmmunity.domain.network.MeecoApi
import com.brainer.itmmunity.domain.network.UnderkgApi
import com.brainer.itmmunity.domain.repository.ContentRepository
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

class MainViewModel(
    application: Application,
) : AndroidViewModel(application) {
    companion object {
        private val contentRepository: ContentRepository = ContentRepository(
            UnderkgDataSource(
                UnderkgApi(),
                Dispatchers.IO,
            ),
            MeecoDataSource(
                MeecoApi(),
                Dispatchers.IO,
            ),
        )
    }

    private val _loadState = MutableStateFlow(LoadState.LOADED)
    val loadState = _loadState.asStateFlow()

    private var _page = MutableStateFlow(0)

    private var _isTabletUi = MutableStateFlow(false)
    val isTabletUi = _isTabletUi.asStateFlow()

    private var _titleString = MutableStateFlow("ITmmunity")
    val titleString = _titleString.asStateFlow()

    private var _newsList = MutableStateFlow(listOf<ContentModel>())
    val newsList = _newsList.asStateFlow()

    init {
        refresh()
    }

    fun fetchLatestNews() {
        if (_loadState.value == LoadState.LOADING) return
        viewModelScope.launch {
            _page.value = _page.value.plus(1)
            _newsList.value += contentRepository.getLatestNewsList(_page.value)
        }.onJoin
    }

    fun refresh() {
        viewModelScope.launch(Dispatchers.IO) {
            _loadState.value = LoadState.LOADING
            _page.value = 0
            _newsList.value = contentRepository.getLatestNewsList(_page.value)
            _loadState.value = LoadState.LOADED
        }.onJoin
    }

    private var _aNews = MutableStateFlow<ContentModel?>(null)
    val aNews: MutableStateFlow<ContentModel?> = _aNews

    fun changeAnews(news: ContentModel) {
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

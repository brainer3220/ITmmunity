package com.brainer.itmmunity.domain.repository

import com.brainer.itmmunity.domain.data.MeecoDataSource
import com.brainer.itmmunity.domain.data.UnderkgDataSource
import com.brainer.itmmunity.domain.model.ContentModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async

class ContentRepository(
    private val underkgDataSource: UnderkgDataSource,
    private val meecoDataSource: MeecoDataSource,
) {
    private var latestNewsList: List<ContentModel> = listOf()

    suspend fun getLatestNewsList(page: Int): List<ContentModel> {
        CoroutineScope(Dispatchers.IO).async {
            latestNewsList += fetchUnderkgNews(page)
            latestNewsList += fetchMeecoNews(page)
        }.await()
        return latestNewsList
    }

    private suspend fun fetchUnderkgNews(page: Int) = underkgDataSource.fetchLatestNews(page)
    private suspend fun fetchMeecoNews(page: Int) = meecoDataSource.fetchLatestNews(page)
}

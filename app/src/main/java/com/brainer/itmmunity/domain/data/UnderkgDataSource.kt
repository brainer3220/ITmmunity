package com.brainer.itmmunity.domain.data

import com.brainer.itmmunity.domain.model.ContentModel
import com.brainer.itmmunity.domain.network.UnderkgApi
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

class UnderkgDataSource(
    private val underkgApi: UnderkgApi,
    private val ioDispatcher: CoroutineDispatcher,
) {
    suspend fun fetchLatestNews(page: Int): List<ContentModel> = withContext(ioDispatcher) {
        underkgApi.fetchLatestNews(page)
    }

    interface NewsApi {
        fun fetchLatestNews(): List<ContentModel>
    }
}

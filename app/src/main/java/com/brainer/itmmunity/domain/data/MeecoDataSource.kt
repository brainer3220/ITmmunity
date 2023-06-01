package com.brainer.itmmunity.domain.data

import com.brainer.itmmunity.domain.model.ContentModel
import com.brainer.itmmunity.domain.network.MeecoApi
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

class MeecoDataSource(
    private val meecoApi: MeecoApi,
    private val ioDipatcher: CoroutineDispatcher,
) {
    suspend fun fetchLatestNews(page: Int) = withContext(ioDipatcher) {
        meecoApi.fetchLatestNews(page)
    }

    interface NewsApi {
        fun fetchLatestNews(): List<ContentModel>
    }
}

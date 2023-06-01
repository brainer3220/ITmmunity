package com.brainer.itmmunity.domain.model

import com.brainer.itmmunity.domain.UrlConsturct.MEECO_URL
import com.brainer.itmmunity.domain.UrlConsturct.UNDERKG_URL

object ContentProviderFactory {
    fun createContentProvider(url: String): ContentProvider {
        return when {
            url.contains(MEECO_URL) -> MeecoNews()
            url.contains(UNDERKG_URL) -> KGNewsContent()
            else -> throw IllegalArgumentException("Unsupported URL")
        }
    }
}

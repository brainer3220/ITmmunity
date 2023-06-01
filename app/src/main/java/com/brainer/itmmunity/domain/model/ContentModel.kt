package com.brainer.itmmunity.domain.model

import android.os.Parcelable
import io.github.furstenheim.CopyDown
import kotlinx.parcelize.Parcelize
import org.jsoup.select.Elements

@Parcelize
data class ContentModel(
    var title: String,
    var image: String? = null,
    var hit: Int,
    var numComment: Int? = null,
    var url: String,
) : Parcelable {

    private fun returnContent(): Pair<String?, Elements?> {
        val contentProvider = ContentProviderFactory.createContentProvider(url)

        val aItemList = contentProvider.getContents(url)
        val aCommentList = contentProvider.getComments(url)

        return Pair(aItemList, aCommentList)
    }

    fun htmlToMarkdown(): String? {
        val converter = CopyDown()
        return converter.convert(returnContent().toString())
    }
}

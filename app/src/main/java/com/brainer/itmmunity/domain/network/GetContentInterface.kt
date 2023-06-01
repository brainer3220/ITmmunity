package com.brainer.itmmunity.domain.network

import android.util.Log
import com.brainer.itmmunity.domain.model.ContentModel
import org.jsoup.Jsoup
import org.jsoup.nodes.Document

interface GetContentInterface {
    /**
     * @author brainer
     * @param url String type
     * @return Document type
     */
    fun getHTML(url: String): Document? {
        val doc = Jsoup.connect(url).get()
        Log.d("getHTML", doc.toString())
        return doc
    }

    fun getItem(url: String, target: String, dTail: String?): List<ContentModel>
    fun fetchLatestNews(page: Int): List<ContentModel>

    fun htmlToMarkdown(html: String): String {
        TODO()
    }
}

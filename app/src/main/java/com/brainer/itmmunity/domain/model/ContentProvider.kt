package com.brainer.itmmunity.domain.model

import android.util.Log
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.jsoup.select.Elements

interface ContentProvider {
    fun returnContent(url: String): Pair<String?, Elements?>
    fun getHTML(url: String): Document? {
        val doc = Jsoup.connect(url).get()
        Log.d("getHTML", doc.toString())
        return doc
    }

    fun getContents(url: String): String
    fun getComments(url: String): Elements?
    fun getItem(url: String, selector: String): Elements?
}

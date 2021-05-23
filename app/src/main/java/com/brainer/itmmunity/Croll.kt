package com.brainer.itmmunity

import android.util.Log
import org.jsoup.Jsoup
import org.jsoup.nodes.Document

class Croll {
    data class Content(
        val title: String,
        val image: String?,
        val hit: Int,
        val numComment: Int,
        val url: String
    )

     fun getHTML(URL: String): Document? {
        val doc = Jsoup.connect(URL).get()
        Log.d("HTML", doc.toString())
        return doc
    }
}

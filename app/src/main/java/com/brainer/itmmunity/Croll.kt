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

     fun getHTML(url: String): Document? {
        val doc = Jsoup.connect(url).get()
        Log.d("HTML", doc.toString())
        return doc
    }

    fun getItem(url: String, target: String, dTail: String): ArrayList<String> {
        val doc = Croll().getHTML(url)
        val newsList = arrayListOf<String>()
        doc?.select(target)?.forEach { i ->
            newsList.add(i.text())
        }
        return newsList
    }
}

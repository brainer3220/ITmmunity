package com.brainer.itmmunity

import android.util.Log
import org.jsoup.Jsoup
import org.jsoup.nodes.Document

class Croll {
    data class Content(
        var title: String,
        var image: String?,
        var hit: Int,
        var numComment: Int,
        var url: String
    )

     fun getHTML(url: String): Document? {
        val doc = Jsoup.connect(url).get()
        Log.d("getHTML", doc.toString())
        return doc
    }

    private fun getItem(url: String, target: String, dTail: String): ArrayList<String> {
        val doc = Croll().getHTML(url)?.select(target)
        val itemList = arrayListOf<String>()
        if (doc != null) {
            Log.i("getItem", "doc is not null")
            doc.forEach { i ->
                Log.d("getItem", "ForEach" + i.text())
                itemList.add(i.text())
            }
        }
        return itemList
    }
}

package com.brainer.itmmunity

import android.util.Log
import org.jsoup.Jsoup
import org.jsoup.nodes.Document

class Croll {
    data class Content(
        var title: String,
        var image: String?,
        var hit: Int,
        var numComment: Int?,
        var url: String
    )

     fun getHTML(url: String): Document? {
        val doc = Jsoup.connect(url).get()
        Log.d("getHTML", doc.toString())
        return doc
    }

    private fun getItem(url: String, target: String, dTail: String): ArrayList<Content> {
        val doc = Croll().getHTML(url)?.select(target)
        val itemList = arrayListOf<Content>()
        if (doc != null) {
            Log.i("getItem null", "doc is not null")
            Log.i("getItem_String", doc.toString())

            doc.forEach { i ->
                Log.d("getItem_title", "ForEach: " + i.select("a").text())
                Log.d("getItem_image", "ForEach: " + i.select("div.thumb-wrap > a > img").attr("src"))
                Log.d("getItem_url", "ForEach: " + i.select("a").attr("href"))
                itemList.add(Content(
                    title=i.select("a").text(),
                    image=i.select("div.thumb-wrap > a > img").attr("src"),
                    hit = 100,
                    numComment = null,
                    url = i.select("a").attr("href")))
            }
        }
        return itemList
    }

    fun returnData(): ArrayList<Content> {
        var aItemList = Croll().getItem("https://www.underkg.co.kr/news", "#board_list > div > div", "Text")

        val itemList = arrayListOf<Content>()
        for (i in aItemList) {
            itemList.add(i)
            Log.i("returnDataItem", i.toString())
        }

        println("returnData$itemList")
        return itemList
    }
}

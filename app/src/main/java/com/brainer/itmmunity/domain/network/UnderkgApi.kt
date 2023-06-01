package com.brainer.itmmunity.domain.network

import android.util.Log
import com.brainer.itmmunity.domain.model.ContentModel

class UnderkgApi : GetContentInterface {
    override fun getItem(url: String, target: String, dTail: String?): List<ContentModel> {
        val doc = getHTML(url)?.select(target)
        val itemList = arrayListOf<ContentModel>()
        if (doc != null) {
            Log.i("getItem null", "doc is not null")
            Log.i("getItem_String", doc.toString())

            doc.forEach { i ->
                Log.d("getItem_title", "ForEach: " + i.select("a").text())
                Log.d(
                    "getItem_image",
                    "ForEach: " + i.select("div.thumb-wrap > a > img").attr("src"),
                )
                Log.d("getItem_url", "ForEach: " + i.select("a").attr("href"))
                itemList.add(
                    ContentModel(
                        title = i.select("a").text(),
                        image = i.select("div.thumb-wrap > a > img").attr("src"),
                        hit = 100,
                        numComment = null,
                        url = i.select("a").attr("href"),
                    ),
                )
            }
        }
        return itemList
    }

    override fun fetchLatestNews(page: Int): List<ContentModel> {
        val itemList = getItem(
            "http://underkg.co.kr/index.php?mid=news&page=$page",
            "#board_list > div > div",
            "Text",
        )

        Log.d("returnData", "$itemList")
        return itemList
    }
}

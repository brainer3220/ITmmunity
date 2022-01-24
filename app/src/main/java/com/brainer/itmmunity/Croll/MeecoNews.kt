package com.brainer.itmmunity.Croll

import android.util.Log

class MeecoNews : Croll() {
    private fun getItem(url: String, target: String): ArrayList<Content> {
        val itemList = arrayListOf<Content>()
        val doc = getHTML(url)?.select(target)
        if (doc != null) {
            Log.i("meecoGetItem_null", "doc is not null")
            Log.i("meecoGetItem_String", doc.toString())
        }

        doc?.forEach { i ->
            Log.d("meecoGetItemTitle", "forEach ${i.select("td.title > a > span").text()}")
            Log.d("meecoGetItemUrl", "forEach ${i.select("td.title > a").attr("href")}")
            Log.d(
                "meecoGetItemNumComment",
                "forEach ${i.toggleClass("num").select("span")[1].text().toInt()}"
            )
            itemList.add(
                Content(
                    title = i.select("td.title > a > span").text(),
                    url = ("https://meeco.kr" + i.select("td.title > a").attr("href")),
                    numComment = i.toggleClass("num").select("span")[1].text().toInt(),
                    hit = i.toggleClass("num").select("span")[2].text().toInt(),
                    image = null
                )
            )
        }
        return itemList
    }

    override fun returnData(page: Int): ArrayList<Content> {
        val itemList = this.getItem(
            "https://meeco.kr/index.php?mid=news&page=${page}",
            "#bBd > div.bBox > div > table > tbody > tr"
        )

        Log.d("MeecoNews_Content", "$itemList")
        return itemList
    }

//    override fun returnContent(content: Content): Pair<Elements?, Elements?> {
//        val aItemList = getHTML(content.url)?.select("#bBd > article")
//        val aCommentList = getHTML(content.url)?.select("#bCmt")
//
//        Log.d("KGNews_Content", "$aItemList")
//        Log.d("KGNews_Comment", "$aCommentList")
//        return Pair(aItemList, aCommentList)
//    }
}

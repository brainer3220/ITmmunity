package com.brainer.itmmunity.Croll

import android.util.Log
import org.jsoup.select.Elements

class KGNewsContent: Croll() {
    fun getItem(url: String, target: String): Elements? {
        val doc = Croll().getHTML(url)?.select(target)
//        val itemList = arrayListOf<Content>()
        if (doc != null) {
            Log.i("getItem null", "doc is not null")
            Log.i("getItem_String", doc.toString())
        }
        return doc
    }

    fun returnData(url: String): Elements? {
        val aItemList = this.getItem(url, "body > div.user_layout > div.body > div.content > div > div.docInner > div.read_body")

//        val itemList = arrayListOf<Content>()
//        for (i in aItemList) {
//            itemList.add(i)
//            Log.i("returnDataItem", i.toString())
//        }

        println("returnData$aItemList")
        return aItemList
    }
}
package com.brainer.itmmunity.Croll

import android.util.Log
import org.jsoup.select.Elements

class KGNewsContent: Croll() {
    fun getItem(url: String, target: String): Elements? {
        val doc = getHTML(url)?.select(target)
        if (doc != null) {
            Log.i("getItem null", "doc is not null")
            Log.i("getItem_String", doc.toString())
        }
        return doc
    }

    fun returnData(url: String): Pair<Elements?, Elements?> {
        val aItemList = this.getItem(url, "body > div.user_layout > div.body > div.content > div > div.docInner > div.read_body")
        val aCommentList = this.getItem(url, "div#comment.feedback")

        Log.d("KGNews_Content", "$aItemList")
        Log.d("KGNews_Comment", "$aCommentList")
        return Pair(aItemList, aCommentList)
    }
}

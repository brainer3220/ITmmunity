package com.brainer.itmmunity.domain.model

import android.util.Log
import org.jsoup.select.Elements

class KGNewsContent : ContentProvider {
    override fun returnContent(url: String): Pair<String?, Elements?> {
        val aItemList = getItem(
            url,
            "body > div.user_layout > div.body > div.content > div > div.docInner > div.read_body",
        )
        val aCommentList = getItem(url, "div#comment.feedback")

        Log.d("KGNews_Content", "$aItemList")
        Log.d("KGNews_Comment", "$aCommentList")
        return Pair(aItemList.toString(), aCommentList)
    }

    override fun getContents(url: String): String {
        val aItemList = getItem(
            url,
            "body > div.user_layout > div.body > div.content > div > div.docInner > div.read_body",
        )
        val aCommentList = getItem(url, "div#comment.feedback")

        Log.d("KGNews_Content", "$aItemList")
        Log.d("KGNews_Comment", "$aCommentList")
        return aItemList.toString()
    }

    override fun getComments(url: String): Elements? {
        val aItemList = getItem(
            url,
            "body > div.user_layout > div.body > div.content > div > div.docInner > div.read_body",
        )
        val aCommentList = getItem(url, "div#comment.feedback")

        Log.d("KGNews_Content", "$aItemList")
        Log.d("KGNews_Comment", "$aCommentList")
        return aCommentList
    }

    override fun getItem(url: String, selector: String): Elements? {
        val doc = getHTML(url)?.select(selector)
        if (doc != null) {
            Log.d("getItem null", "doc is not null")
            Log.d("getItem_String", doc.toString())
        }
        return doc
    }
}

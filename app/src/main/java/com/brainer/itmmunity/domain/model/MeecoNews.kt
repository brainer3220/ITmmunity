package com.brainer.itmmunity.domain.model

import android.util.Log
import com.brainer.itmmunity.domain.UrlConsturct.MEECO_URL
import org.jsoup.select.Elements

class MeecoNews : ContentProvider {
    override fun returnContent(url: String): Pair<String?, Elements?> {
        Log.d("Meeco_URL", url)
        val aItemList = MeecoNews().getHTML(url)?.select("article > div")
        val aCommentList = MeecoNews().getHTML(url)?.select("#comment")
        val aItems =
            aItemList.toString().replace("//img.$MEECO_URL/", "https://img.$MEECO_URL/")

        Log.d("MeecoNews_Content", aItems)
        Log.d("MeecoNews_Comment", "$aCommentList")
        return Pair(aItems, aCommentList)
    }

    override fun getContents(url: String): String {
        Log.d("Meeco_URL", url)
        val aItemList = MeecoNews().getHTML(url)?.select("article > div")
        val aItems =
            aItemList.toString().replace("//img.$MEECO_URL/", "https://img.$MEECO_URL/")

        Log.d("MeecoNews_Content", aItems)
        return aItems
    }

    override fun getComments(url: String): Elements? {
        Log.d("Meeco_URL", url)
        val aCommentList = MeecoNews().getHTML(url)?.select("#comment")

        Log.d("MeecoNews_Comment", "$aCommentList")
        return aCommentList
    }

    override fun getItem(url: String, selector: String): Elements? {
        // Implementation specific to MeecoNews
        return TODO("Provide the return value")
    }
}

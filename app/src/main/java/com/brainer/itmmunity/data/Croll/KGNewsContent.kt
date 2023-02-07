package com.brainer.itmmunity.data.Croll

import android.util.Log
import org.jsoup.select.Elements

class KGNewsContent : Croll() {
    fun getItem(url: String, target: String): Elements? {
        val doc = getHTML(url)?.select(target)
        if (doc != null) {
            Log.d("getItem null", "doc is not null")
            Log.d("getItem_String", doc.toString())
        }
        return doc
    }
}

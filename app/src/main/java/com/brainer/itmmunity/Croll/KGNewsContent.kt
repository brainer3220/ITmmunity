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
}

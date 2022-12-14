package com.brainer.itmmunity.data.Croll

import android.os.Parcelable
import android.util.Log
import io.github.furstenheim.CopyDown
import kotlinx.android.parcel.Parcelize
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.jsoup.select.Elements

const val UNDERKG_URL = "underkg.co.kr"
const val MEECO_URL = "meeco.kr"

open class Croll {
    @Parcelize
    data class Content(
        var title: String,
        var image: String? = null,
        var hit: Int,
        var numComment: Int? = null,
        var url: String
    ) : Parcelable {
        private fun returnContent(): Pair<String?, Elements?> {
            val content = this
            when {
                content.url.contains(MEECO_URL) -> {
                    Log.d("Meeco_URL", content.url)
                    val aItemList = MeecoNews().getHTML(content.url)?.select("article > div")
                    val aCommentList = MeecoNews().getHTML(content.url)?.select("#comment")
                    val aItems =
                        aItemList.toString().replace("//img.$MEECO_URL/", "https://img.$MEECO_URL/")

                    Log.d("MeecoNews_Content", aItems)
                    Log.d("MeecoNews_Comment", "$aCommentList")
                    return Pair(aItems, aCommentList)
                }
                content.url.contains(UNDERKG_URL) -> {
                    val aItemList = KGNewsContent().getItem(
                        content.url,
                        "body > div.user_layout > div.body > div.content > div > div.docInner > div.read_body"
                    )
                    val aCommentList = KGNewsContent().getItem(content.url, "div#comment.feedback")

                    Log.d("KGNews_Content", "$aItemList")
                    Log.d("KGNews_Comment", "$aCommentList")
                    return Pair(aItemList.toString(), aCommentList)
                }
                else -> {
                    Log.d("Failed_URL", content.url)
                    return Pair(null, null)
                }
            }
        }

        fun htmlToMarkdown(): String? {
            val converter = CopyDown()
            return converter.convert(returnContent().toString())
        }
    }

    /**
     * @author brainer
     * @param url String type
     * @return Document type
     */
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
                Log.d(
                    "getItem_image",
                    "ForEach: " + i.select("div.thumb-wrap > a > img").attr("src")
                )
                Log.d("getItem_url", "ForEach: " + i.select("a").attr("href"))
                itemList.add(
                    Content(
                        title = i.select("a").text(),
                        image = i.select("div.thumb-wrap > a > img").attr("src"),
                        hit = 100,
                        numComment = null,
                        url = i.select("a").attr("href")
                    )
                )
            }
        }
        return itemList
    }

    open fun returnData(page: Int = 1): ArrayList<Content> {
        val itemList = Croll().getItem(
            "http://underkg.co.kr/index.php?mid=news&page=${page}",
            "#board_list > div > div",
            "Text"
        )

        Log.d("returnData", "$itemList")
        return itemList
    }
}

package com.brainer.itmmunity.Croll

import android.os.Parcelable
import android.util.Log
import io.github.furstenheim.CopyDown
import kotlinx.android.parcel.Parcelize
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.jsoup.select.Elements


open class Croll {
    @Parcelize
    data class Content(
        var title: String,
        var image: String?,
        var hit: Int,
        var numComment: Int?,
        var url: String
    ) : Parcelable {
        fun returnContent(content: Content): Pair<String?, Elements?> {
            when {
                content.url.contains("meeco.kr") -> {
                    Log.d("Meeco_URL", content.url)
                    val aItemList = MeecoNews().getHTML(content.url)?.select("article > div")
                    val aCommentList = MeecoNews().getHTML(content.url)?.select("#comment")
                    val aItems = aItemList.toString().replace("//img.meeco.kr/", "https://img.meeco.kr/")

                    Log.d("MeecoNews_Content", aItems)
                    Log.d("MeecoNews_Comment", "$aCommentList")
                    return Pair(aItems, aCommentList)
                }
                content.url.contains("underkg.co.kr") -> {
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

        open fun htmlToMarkdown(content: Content): String? {
            val converter = CopyDown()
            return converter.convert(returnContent(content).toString())
        }
    }

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

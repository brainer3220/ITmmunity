package com.brainer.itmmunity.domain.network

import com.brainer.itmmunity.domain.UrlConsturct.MEECO_URL
import com.brainer.itmmunity.domain.model.ContentModel

class MeecoApi : GetContentInterface {
    override fun getItem(url: String, target: String, dTail: String?): List<ContentModel> {
        val doc = getHTML(url)?.select(target) ?: return arrayListOf()
        return doc.mapNotNull { i ->
            ContentModel(
                title = i.select("td.title > a > span").text(),
                url = ("https://$MEECO_URL" + i.select("td.title > a").attr("href")),
                numComment = i.toggleClass("num").select("span")[1].text().toIntOrNull() ?: 0,
                hit = i.toggleClass("num").select("span")[2].text().toIntOrNull() ?: 0,
                image = null,
            )
        }
    }

    override fun fetchLatestNews(page: Int): List<ContentModel> {
        return this.getItem(
            "https://$MEECO_URL/index.php?mid=news&page=$page",
            "#bBd > div.bBox > div > table > tbody > tr",
            null,
        )
    }
}

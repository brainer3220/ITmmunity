package com.brainer.itmmunity.data.Croll

class MeecoNews : Croll() {
    private fun getItem(url: String, target: String): ArrayList<Content> {
        val doc = getHTML(url)?.select(target) ?: return arrayListOf()
        return doc.mapNotNull { i ->
            Content(
                title = i.select("td.title > a > span").text(),
                url = ("https://$MEECO_URL" + i.select("td.title > a").attr("href")),
                numComment = i.toggleClass("num").select("span")[1].text().toIntOrNull() ?: 0,
                hit = i.toggleClass("num").select("span")[2].text().toIntOrNull() ?: 0,
                image = null,
            )
        }.toCollection(arrayListOf())
    }

    override fun returnData(page: Int): ArrayList<Content> {
        return this.getItem(
            "https://$MEECO_URL/index.php?mid=news&page=$page",
            "#bBd > div.bBox > div > table > tbody > tr",
        )
    }
}

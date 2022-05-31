package com.brainer.itmmunity.navcontrol

const val DEFAULT_NEWS_URL = "news_url"

sealed class NavScreen(val route: String) {
    object MainList : NavScreen(route = "main_list")
    object ContentView : NavScreen(route = "content_view/{$DEFAULT_NEWS_URL}") {
        fun passNewsUrl(url: String): String {
            return this.route.replace(oldValue = "{$DEFAULT_NEWS_URL}", newValue = url)
        }
    }
}

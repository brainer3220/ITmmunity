package com.brainer.underkg


data class Content(
        val title: String,
        val image: String?,
        val hit: Int,
        val numComment: Int,
        val url: String
)

val dummies = listOf(Content(title = "삼성전자, 갤럭시 엑스커버 프로 원 UI 3.0 업데이트 배포", numComment= 0, hit = 784, image = "underkg.co.kr", url = "underkg.co.kr"), Content(title = "삼성전자, 갤럭시 엑스커버 프로 원 UI 3.0 업데이트 배포", numComment= 0, hit = 784, image = "underkg.co.kr", url = "underkg.co.kr"))

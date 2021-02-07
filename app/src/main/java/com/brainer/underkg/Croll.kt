package com.brainer.underkg

import org.jsoup.Jsoup

val home = Jsoup.connect("http://underkg.co.kr").get()
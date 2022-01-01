package com.brainer.itmmunity.ViewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.brainer.itmmunity.Croll.Croll
import org.jsoup.Jsoup

class ContentViewModel {
    private var _contentHtml = MutableLiveData("")
    val contentHtml: LiveData<String> = _contentHtml

    private var _aNews = MutableLiveData(Croll.Content(title="", image=null, hit=0, numComment=0, url=""))
    val aNews: LiveData<Croll.Content> = _aNews

    fun getHtml(url: String) {
        val doc = Jsoup.connect(url).get()
        Log.d("getHTML", doc.toString())
        _contentHtml.value = doc.toString()
    }
}
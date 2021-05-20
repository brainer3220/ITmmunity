package com.brainer.itmmunity

import android.app.Activity
import android.os.Bundle
import androidx.recyclerview.widget.RecyclerView
import com.brainer.itmmunity.databinding.ActivityMainBinding

class MainActivity : Activity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val dummies = listOf(
            Content(
                title = "삼성전자, 갤럭시 엑스커버 프로 원 UI 3.0 업데이트 배포",
                numComment = 0,
                hit = 784,
                image = "underkg.co.kr",
                url = "underkg.co.kr"
            ),
            Content(
                title = "삼성전자, 갤럭시 엑스커버 프로 원 UI 3.0 업데이트 배포",
                numComment = 0,
                hit = 784,
                image = "underkg.co.kr",
                url = "underkg.co.kr"
            )
        )

        val recMainView: RecyclerView = findViewById(R.id.main_list)

        val mAdapter = CustomAdapter(dummies)
        recMainView.adapter = mAdapter
    }
}

package com.sstudio.madesubmissionmoviecatalogue

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_detail.*

class DetailActivity : AppCompatActivity() {

    companion object {
        const val EXTRA_DETAIL = "extra_detail"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        val movie = intent.getParcelableExtra<Movie>(EXTRA_DETAIL)
        txt_title.text = movie.title
        txt_overview.text = movie.overview
        img_poster.setImageResource(movie.poster)

    }
}

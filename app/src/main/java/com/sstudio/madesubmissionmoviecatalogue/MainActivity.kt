package com.sstudio.madesubmissionmoviecatalogue

import android.content.res.TypedArray
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private lateinit var dataTitle: Array<String>
    private lateinit var dataOverview: Array<String>
    private lateinit var dataPoster: TypedArray
    private lateinit var adapter: MovieAdapter
    private lateinit var movies: ArrayList<Movie>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        adapter = MovieAdapter(this)
        lv_list.adapter = adapter

        prepare()
        addItem()
    }

    private fun addItem() {
        movies = ArrayList()

        for (i in 0 until dataTitle.size) {
            val movie = Movie(dataTitle[i], dataOverview[i], dataPoster.getResourceId(i, -1))
            movies.add(movie)
        }
        adapter.movies = movies
    }

    private fun prepare() {
        dataTitle = resources.getStringArray(R.array.data_title)
        dataOverview = resources.getStringArray(R.array.data_overview)
        dataPoster = resources.obtainTypedArray(R.array.data_poster)
    }
}

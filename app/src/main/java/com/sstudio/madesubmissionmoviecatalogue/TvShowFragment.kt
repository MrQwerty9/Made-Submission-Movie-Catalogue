package com.sstudio.madesubmissionmoviecatalogue

import android.content.res.TypedArray
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.fragment_tvshow.view.*

class TvShowFragment : Fragment() {

    private lateinit var dataTitle: Array<String>
    private lateinit var dataOverview: Array<String>
    private lateinit var dataPoster: TypedArray
    private lateinit var adapter: MovieAdapter
    private lateinit var movies: ArrayList<Movie>
    private var mView: View? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        mView = inflater.inflate(R.layout.fragment_tvshow, container, false)
        activity?.let { adapter = MovieAdapter(it) }
        mView?.let {
            it.rv_list_tv_show.adapter = adapter
            it.rv_list_tv_show.layoutManager = LinearLayoutManager(context)
        }

        prepare()
        addItem()

        return mView
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
        dataTitle = resources.getStringArray(R.array.data_title_tvshow)
        dataOverview = resources.getStringArray(R.array.data_overview_tvshow)
        dataPoster = resources.obtainTypedArray(R.array.data_poster_tvshow)
    }

}

package com.sstudio.madesubmissionmoviecatalogue

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import kotlinx.android.synthetic.main.item_movie.view.*

class MovieAdapter(val context: Context): BaseAdapter() {

    var movies: ArrayList<Movie> = ArrayList()

    override fun getView(i: Int, view: View?, viewGroup: ViewGroup?): View {
        var mView = view
        if (view == null) {
            mView = LayoutInflater.from(context).inflate(R.layout.item_movie, viewGroup, false)
        }

        val viewHolder = mView?.let { ViewHolder(it) }
        val movie = getItem(i) as Movie
        viewHolder?.bind(movie)
        return mView as View
    }

    override fun getItem(p0: Int): Any {
        return movies[p0]
    }

    override fun getItemId(p0: Int): Long {
        return p0.toLong()
    }

    override fun getCount(): Int {
        return movies.size
    }

    private inner class ViewHolder internal constructor(view: View) {
        private val txtTitle = view.txt_title
        private val txtOverview = view.txt_overview
        private val imgPoster= view.img_poster
        private val cardMovie = view.cv_item_movie

        internal fun bind(movie: Movie) {
            txtTitle.text = movie.title
            txtOverview.text = movie.overvie
            imgPoster.setImageResource(movie.poster)
            cardMovie.setOnClickListener {
                val intent = Intent(context, DetailActivity::class.java)
                intent.putExtra(DetailActivity.EXTRA_DETAIL, movie)
                context.startActivity(intent)
            }
        }
    }
}
package com.sstudio.madesubmissionmoviecatalogue.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.sstudio.madesubmissionmoviecatalogue.BuildConfig
import com.sstudio.madesubmissionmoviecatalogue.mvp.MovieClickAnim
import com.sstudio.madesubmissionmoviecatalogue.R
import com.sstudio.madesubmissionmoviecatalogue.model.MovieTv
import kotlinx.android.synthetic.main.item_movie.view.*


class MovieTvAdapter(private val context: Context, private val isMovie: Boolean, private val movieClickAnim: MovieClickAnim) :
    RecyclerView.Adapter<MovieTvAdapter.ViewHolder>() {

    var movieTv = ArrayList<MovieTv>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_movie, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return movieTv.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val movie = movieTv[position]
        movie.isMovie = if (isMovie) 1 else 0
        if (isMovie) {
            holder.txtTitle.text = movie.title
            holder.txtReleaseDate.text = movie.releaseDate
        }else{
            holder.txtTitle.text = movie.name
            holder.txtReleaseDate.text = movie.firstAirDate
        }
        holder.txtOverview.text = movie.overview
        val poster = BuildConfig.POSTER_LIST + movie.posterPath
        Glide.with(context)
            .load(poster)
            .thumbnail(0.5f)
            .skipMemoryCache(true)
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .placeholder(R.drawable.ic_cloud_download_grey_24dp)
            .into(holder.imgPoster)
        holder.txtRating.text = movie.voteAverage.toString()
        holder.cardMovie.setOnClickListener {
            movieClickAnim.onMovieClick(movieTv[position], holder.imgPoster)
        }
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val txtTitle: TextView = view.txt_title
        val txtOverview: TextView = view.txt_overview
        val imgPoster: ImageView = view.img_poster
        val cardMovie: CardView = view.cv_item_movie
        val txtReleaseDate: TextView = view.txt_release_date
        val txtRating: TextView = view.tv_rating_item
    }

}
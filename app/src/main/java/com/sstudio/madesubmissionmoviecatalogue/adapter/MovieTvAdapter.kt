package com.sstudio.madesubmissionmoviecatalogue.adapter

import android.content.Context
import android.content.Intent
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
import com.sstudio.madesubmissionmoviecatalogue.R
import com.sstudio.madesubmissionmoviecatalogue.model.MovieTv
import com.sstudio.madesubmissionmoviecatalogue.mvp.detail.DetailActivity
import kotlinx.android.synthetic.main.item_movie.view.*


class MovieTvAdapter(private val context: Context, private val isMovie: Boolean) :
    RecyclerView.Adapter<MovieTvAdapter.ViewHolder>() {

    var movieTv: List<MovieTv> = ArrayList()

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
        val poster = BuildConfig.POSTER + movie.posterPath
        Glide.with(context)
            .load(poster)
            .placeholder(R.drawable.ic_cloud_download_grey_24dp)
            .into(holder.imgPoster)
        holder.cardMovie.setOnClickListener {
            val intent = Intent(context, DetailActivity::class.java)
            intent.putExtra(DetailActivity.EXTRA_DETAIL, movie)
            context.startActivity(intent)
        }
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val txtTitle: TextView = view.txt_title
        val txtOverview: TextView = view.txt_overview
        val imgPoster: ImageView = view.img_poster
        val cardMovie: CardView = view.cv_item_movie
        val txtReleaseDate: TextView = view.txt_release_date
    }

}
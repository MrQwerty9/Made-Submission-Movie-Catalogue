package com.sstudio.madesubmissionmoviecatalogue

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.TextureView
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.item_movie.view.*

class MovieAdapter(private val context: Context): RecyclerView.Adapter<MovieAdapter.ViewHolder>() {

    var movies: ArrayList<Movie> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_movie, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return movies.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val movie = movies[position]
        holder.txtTitle.text = movie.title
        holder.txtOverview.text = movie.overview
        holder.imgPoster.setImageResource(movie.poster)
        holder.cardMovie.setOnClickListener {
            val intent = Intent(context, DetailActivity::class.java)
            intent.putExtra(DetailActivity.EXTRA_DETAIL, movie)
            context.startActivity(intent)
        }
    }

    class ViewHolder(view: View): RecyclerView.ViewHolder(view) {
        val txtTitle: TextView = view.txt_title
        val txtOverview: TextView  = view.txt_overview
        val imgPoster: ImageView = view.img_poster
        val cardMovie: CardView = view.cv_item_movie
    }

}
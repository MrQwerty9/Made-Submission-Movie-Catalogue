package com.sstudio.madesubmissionmoviecatalogue.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.sstudio.madesubmissionmoviecatalogue.Common
import com.sstudio.madesubmissionmoviecatalogue.R
import com.sstudio.madesubmissionmoviecatalogue.model.Genres
import com.sstudio.madesubmissionmoviecatalogue.mvp.detail.DetailActivity
import kotlinx.android.synthetic.main.item_genre.view.*
import java.util.*

class GenreAdapter(val isHomeFragment: Boolean, val iGenreAdapterView: IGenreAdapterView?) : RecyclerView.Adapter<GenreAdapter.ViewHolder>(){
    private var genres: List<Genres.Genre> = ArrayList()
    private var isDetailActivity = false
    lateinit var context: Context
    lateinit var parent: ViewGroup
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(genres[position])
    }

    override fun getItemCount(): Int {
        return genres.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        this.context = parent.context
//        this.parent = parent
        if (parent.parent.javaClass.name == DetailActivity().javaClass.name){
            isDetailActivity = true
        }
        val root = (LayoutInflater.from(parent.context).inflate(R.layout.item_genre, parent, false))
        return ViewHolder(root)
    }

    fun addGenre(genres: List<Genres.Genre>?) {
        if (genres != null) {
            this.genres = genres
            notifyDataSetChanged()
        }
    }

    inner class ViewHolder(root: View) : RecyclerView.ViewHolder(root) {

        fun bind(genres: Genres.Genre) = with(itemView) {
            genre_name?.text = genres.name
            genre_name.setOnClickListener {
                if (isHomeFragment){
                    Common.genreSelected.id = genres.id
                    Common.genreSelected.name = genres.name
                    iGenreAdapterView?.genreClicked(genres.id)
                }
            }
        }
    }
}
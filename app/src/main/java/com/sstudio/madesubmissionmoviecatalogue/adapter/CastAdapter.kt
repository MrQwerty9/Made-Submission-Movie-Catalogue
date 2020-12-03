package com.sstudio.madesubmissionmoviecatalogue.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.sstudio.madesubmissionmoviecatalogue.BuildConfig
import com.sstudio.madesubmissionmoviecatalogue.R
import com.sstudio.madesubmissionmoviecatalogue.model.Cast
import kotlinx.android.synthetic.main.item_cast.view.*
import java.util.*

class CastAdapter : RecyclerView.Adapter<CastAdapter.ViewHolder>(){
    private var casts: List<Cast> = ArrayList()

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(casts[position])
    }

    override fun getItemCount(): Int {
        return casts.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val root = (LayoutInflater.from(parent.context).inflate(R.layout.item_cast, parent, false))
        return ViewHolder(root)
    }

    fun addCast(casts: List<Cast>?) {
        if (casts != null) {
            this.casts = casts
            notifyDataSetChanged()
        }
    }

    inner class ViewHolder(root: View) : RecyclerView.ViewHolder(root) {
        fun bind(cast: Cast) = with(itemView) {
            tv_cast_name.text = cast.name
            tv_cast_character.text = cast.character
            Glide.with(this)
                .load(BuildConfig.POSTER_DETAIL + cast.profilPath)
                .skipMemoryCache(true)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .fitCenter()
                .placeholder(R.drawable.ic_account_circle_24dp)
                .into(iv_cast)
        }
    }
}
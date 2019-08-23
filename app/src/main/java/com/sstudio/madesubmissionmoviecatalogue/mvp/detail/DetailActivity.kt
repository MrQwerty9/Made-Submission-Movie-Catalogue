package com.sstudio.madesubmissionmoviecatalogue.mvp.detail

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.bumptech.glide.Glide
import com.sstudio.madesubmissionmoviecatalogue.BuildConfig
import com.sstudio.madesubmissionmoviecatalogue.R
import com.sstudio.madesubmissionmoviecatalogue.model.MovieTv
import kotlinx.android.synthetic.main.activity_detail.*
import kotlinx.android.synthetic.main.content_detail.*


class DetailActivity : AppCompatActivity() {

    companion object {
        const val EXTRA_DETAIL = "extra_detail"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)
        setupToolbar()
        val movieTv = intent.getParcelableExtra<MovieTv>(EXTRA_DETAIL)
        val poster = BuildConfig.POSTER + movieTv.posterPath
        val hello =
            String.format(resources.getString(R.string.rating), movieTv.voteAverage.toString(), movieTv.voteCount.toString())

        loading_progress.visibility = View.VISIBLE
        txt_overview.text = movieTv.overview
        txt_rating.text = hello
        Glide.with(this)
            .load(poster)
            .placeholder(R.drawable.ic_cloud_download_grey_24dp)
            .into(img_poster)
        if (movieTv.isMovie){
            collapsingToolbar.title = movieTv.title
            txt_release_date.text = movieTv.releaseDate
        }else{
            collapsingToolbar.title = movieTv.name
            txt_release_date.text = movieTv.firstAirDate
        }
        loading_progress.visibility = View.GONE
    }

    private fun setupToolbar(){
        setSupportActionBar(appbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        collapsingToolbar.setExpandedTitleMargin(50, 50, 250, 50)
    }
}

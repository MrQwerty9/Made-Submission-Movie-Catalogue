package com.sstudio.madesubmissionmoviecatalogue.mvp.detail

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.sstudio.madesubmissionmoviecatalogue.App
import com.sstudio.madesubmissionmoviecatalogue.BuildConfig
import com.sstudio.madesubmissionmoviecatalogue.R
import com.sstudio.madesubmissionmoviecatalogue.model.MovieTv
import com.sstudio.madesubmissionmoviecatalogue.mvp.detail.presenter.DetailPresenter
import kotlinx.android.synthetic.main.activity_detail.*
import kotlinx.android.synthetic.main.content_detail.*
import kotlinx.android.synthetic.main.movie_wrapper.*
import javax.inject.Inject

class DetailActivity : AppCompatActivity(), DetailView {

    @Inject
    lateinit var detailPresenter: DetailPresenter
    private lateinit var movieTv: MovieTv

    companion object {
        const val EXTRA_DETAIL = "extra_detail"
        var isShowFavorite = false
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)
        (this.applicationContext as App).createFavoriteComponent(this).inject(this)
        setupToolbar()
        showMovieDetail()
        btn_favorite.setOnClickListener(favoriteOnClick)
    }

    private val favoriteOnClick = View.OnClickListener {
        if (isShowFavorite) {
            detailPresenter.removeFavorite(movieTv.id)
        } else {
            detailPresenter.addFavorite(movieTv)
        }
    }

    private fun showMovieDetail() {
        movieTv = intent.getParcelableExtra(EXTRA_DETAIL)
        val poster = BuildConfig.POSTER + movieTv.posterPath
        val rating =
            String.format(
                resources.getString(R.string.rating),
                movieTv.voteAverage.toString(),
                movieTv.voteCount.toString()
            )
        detailPresenter.loadFavorite(movieTv.id)
        progressVisible()
        txt_overview.text = movieTv.overview
        txt_rating.text = rating
        Glide.with(this)
            .load(poster)
            .dontAnimate()
            .fitCenter()
            .placeholder(R.drawable.ic_cloud_download_grey_24dp)
            .into(img_poster)
        if (movieTv.isMovie == 1) {
            collapsingToolbar.title = movieTv.title
            txt_release_date.text = movieTv.releaseDate
        } else {
            collapsingToolbar.title = movieTv.name
            txt_release_date.text = movieTv.firstAirDate
        }
        progressGone()
    }

    private fun setupToolbar() {
        setSupportActionBar(appbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        collapsingToolbar.setExpandedTitleMargin(50, 50, 250, 50)
    }

    override fun isShowFavorite(isFav: Boolean) {
        if (isFav) {
            isShowFavorite = true
            btn_favorite.setImageResource(R.drawable.ic_favorite_pink_24dp)
        } else {
            isShowFavorite = false
            btn_favorite.setImageResource(R.drawable.ic_favorite_white_24dp)
        }
    }


    override fun toast(text: String?) {
        Toast.makeText(this, "$text", Toast.LENGTH_SHORT).show()
    }

    override fun onDestroy() {
        super.onDestroy()
        detailPresenter.dumpData()
    }

    private fun progressVisible() {
        loading_progress.visibility = View.VISIBLE
    }

    private fun progressGone() {
        loading_progress.visibility = View.GONE
    }
}

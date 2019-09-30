package com.sstudio.madesubmissionmoviecatalogue.mvp.detail

import android.appwidget.AppWidgetManager
import android.content.ComponentName
import android.database.ContentObserver
import android.database.Cursor
import android.net.Uri
import android.os.AsyncTask
import android.os.Bundle
import android.os.Handler
import android.os.HandlerThread
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.net.toUri
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.sstudio.madesubmissionmoviecatalogue.App
import com.sstudio.madesubmissionmoviecatalogue.BuildConfig
import com.sstudio.madesubmissionmoviecatalogue.FavoriteWidget
import com.sstudio.madesubmissionmoviecatalogue.R
import com.sstudio.madesubmissionmoviecatalogue.data.local.FavoriteDb
import com.sstudio.madesubmissionmoviecatalogue.model.MovieTv
import com.sstudio.madesubmissionmoviecatalogue.mvp.detail.presenter.DetailPresenter
import com.sstudio.madesubmissionmoviecatalogue.mvp.detail.presenter.DetailPresenterImpl
import com.sstudio.madesubmissionmoviecatalogue.mvp.movie.view.MovieFragment
import kotlinx.android.synthetic.main.activity_detail.*
import kotlinx.android.synthetic.main.content_detail.*
import kotlinx.android.synthetic.main.movie_wrapper.*
import java.lang.ref.WeakReference
import javax.inject.Inject

class DetailActivity : AppCompatActivity(), DetailView {

    @Inject
    lateinit var detailPresenter: DetailPresenter
    private lateinit var movieTv: MovieTv
    private lateinit var myObserver: DataObserver
    private lateinit var handlerThread: HandlerThread
    var uri: Uri? = null

    companion object {
        const val EXTRA_DETAIL = "extra_detail"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)
        (this.applicationContext as App).createFavoriteComponent(this).inject(this)
        setupToolbar()
        showMovieDetail()
        btn_favorite.setOnClickListener(favoriteOnClick)
        initHandler()
    }

    private val favoriteOnClick = View.OnClickListener {
        detailPresenter.favoriteClick(movieTv)
        uri?.let { LoadFavoriteAsync(detailPresenter, movieTv.id, it).execute() }
    }

    private fun initHandler() {
        handlerThread = HandlerThread("DataObserver")
        handlerThread.start()
        val handler = Handler(handlerThread.looper)
        uri?.let {
            myObserver = DataObserver(handler, detailPresenter, movieTv.id, it)
            this.contentResolver.registerContentObserver(FavoriteDb.CONTENT_URI, true, myObserver)
        }
    }

    private fun showMovieDetail() {
        movieTv = intent.getParcelableExtra(EXTRA_DETAIL)
        uri = intent.data
        val uri = intent.data
        val poster = (BuildConfig.POSTER_DETAIL + movieTv.posterPath).toUri()
        val rating =
            String.format(
                resources.getString(R.string.rating),
                movieTv.voteAverage.toString(),
                movieTv.voteCount.toString()
            )
//        detailPresenter.loadFavorite(movieTv.id)
        uri?.let { LoadFavoriteAsync(detailPresenter, movieTv.id, it).execute() }
        progressVisible()
        txt_overview.text = movieTv.overview
        txt_rating.text = rating
        Glide.with(this)
            .load(poster)
            .dontAnimate()
            .skipMemoryCache(true)
            .diskCacheStrategy(DiskCacheStrategy.ALL)
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

    override fun isShowFavorite(resource: Int) {
        btn_favorite.setImageResource(resource)
    }


    override fun toast(text: String?) {
        Toast.makeText(this, "$text", Toast.LENGTH_SHORT).show()
    }

    override fun onDestroy() {
        super.onDestroy()
        detailPresenter.dumpData()
        Log.d("mytag", "ondestroy")
        Glide.get(this).clearMemory()
    }


    private fun progressVisible() {
        loading_progress.visibility = View.VISIBLE
    }

    private fun progressGone() {
        loading_progress.visibility = View.GONE
    }

    internal class LoadFavoriteAsync(
        detailPresenter: DetailPresenter,
        private val id: Int,
        private val uri: Uri
    ) : AsyncTask<Void, Void, Cursor>() {

        private val weakCallback = WeakReference(detailPresenter)

        override fun doInBackground(vararg p0: Void?): Cursor? {
            return weakCallback.get()?.loadFavoriteProvider(id, uri)
        }

        override fun onPostExecute(result: Cursor?) {
            super.onPostExecute(result)
            result?.let { weakCallback.get()?.setFavButton(it) }
        }
    }


    class DataObserver(handler: Handler, private val detailPresenter: DetailPresenter, private val id: Int,
                       private val uri: Uri) :
        ContentObserver(handler) {

        override fun onChange(selfChange: Boolean) {
            super.onChange(selfChange)
            LoadFavoriteAsync(detailPresenter, id, uri).execute()
        }
    }
}

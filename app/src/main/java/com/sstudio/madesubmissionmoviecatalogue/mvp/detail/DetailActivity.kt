package com.sstudio.madesubmissionmoviecatalogue.mvp.detail

import android.content.Context
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
import com.sstudio.madesubmissionmoviecatalogue.FavoriteAsyncCallback
import com.sstudio.madesubmissionmoviecatalogue.R
import com.sstudio.madesubmissionmoviecatalogue.data.local.FavoriteDb
import com.sstudio.madesubmissionmoviecatalogue.model.MovieTv
import com.sstudio.madesubmissionmoviecatalogue.mvp.detail.presenter.DetailPresenter
import kotlinx.android.synthetic.main.activity_detail.*
import kotlinx.android.synthetic.main.content_detail.*
import kotlinx.android.synthetic.main.movie_wrapper.*
import java.lang.ref.WeakReference
import javax.inject.Inject
import android.os.Looper


class DetailActivity : AppCompatActivity(), DetailView, FavoriteAsyncCallback {

    @Inject
    lateinit var detailPresenter: DetailPresenter

    private lateinit var myObserver: DataObserver
    private lateinit var handlerThread: HandlerThread
    var uri: Uri? = null

    companion object {
        const val EXTRA_DETAIL = "extra_detail"
        lateinit var movieTv: MovieTv
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)
        initHandler()
        (this.applicationContext as App).createFavoriteComponent(this).inject(this)
        setupToolbar()
        showMovieDetail()
        btn_favorite.setOnClickListener(favoriteOnClick)

    }

    private val favoriteOnClick = View.OnClickListener {
        DoFavoriteAsync(this, false).execute()
//        uri?.let { LoadFavoriteAsync(detailPresenter, movieTv.id, it).execute() }
//        uri?.let { it1 -> detailPresenter.favoriteClick(movieTv, it1) }
    }

    private fun initHandler() {
        handlerThread = HandlerThread("DataObserver")
        handlerThread.start()
        val handler = Handler(Looper.getMainLooper())
//        val handler = Handler(handlerThread.looper)
        myObserver = DataObserver(handler, this, true)
        this.contentResolver.registerContentObserver(FavoriteDb.CONTENT_URI, true, myObserver)

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
        uri?.let { DoFavoriteAsync(this, true).execute() }
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
        Glide.get(this).clearMemory()
    }


    private fun progressVisible() {
        loading_progress.visibility = View.VISIBLE
    }

    private fun progressGone() {
        loading_progress.visibility = View.GONE
    }

    override fun doInBackground(isLoadFavorite: Boolean): Cursor? {
        return if (isLoadFavorite) {
            uri?.let { detailPresenter.loadFavoriteProvider(it) }
        } else {
            uri?.let { detailPresenter.favoriteClick(movieTv, it) }
            null
        }
    }

    override fun onPostExecute(cursor: Cursor?, isLoadFavorite: Boolean) {
        if (isLoadFavorite) {
            cursor?.let { detailPresenter.setFavButton(it) }
        } else {
            contentResolver.notifyChange(
                FavoriteDb.CONTENT_URI, DataObserver(
                    Handler(), this, false
                )
            )
        }
    }

    internal class DoFavoriteAsync(
        favoriteAsyncCallback: FavoriteAsyncCallback,
        private val isLoadFavorite: Boolean
    ) : AsyncTask<Void, Void, Cursor>() {

        private val weakCallback = WeakReference(favoriteAsyncCallback)

        override fun doInBackground(vararg p0: Void?): Cursor? {
            return weakCallback.get()?.doInBackground(isLoadFavorite)
        }

        override fun onPostExecute(result: Cursor?) {
            super.onPostExecute(result)
            weakCallback.get()?.onPostExecute(result, isLoadFavorite)
        }
    }

    class DataObserver(
        handler: Handler,
        private val context: Context,
        private val isLoadFavorite: Boolean
    ) : ContentObserver(handler) {

        override fun onChange(selfChange: Boolean) {
            super.onChange(selfChange)
            DoFavoriteAsync(context as FavoriteAsyncCallback, isLoadFavorite).execute()
        }
    }
}

package com.sstudio.madesubmissionmoviecatalogue.mvp.detail

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.database.ContentObserver
import android.database.Cursor
import android.net.ConnectivityManager
import android.net.Uri
import android.os.AsyncTask
import android.os.Bundle
import android.os.Handler
import android.os.HandlerThread
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.google.android.material.appbar.AppBarLayout
import com.sstudio.madesubmissionmoviecatalogue.App
import com.sstudio.madesubmissionmoviecatalogue.BuildConfig
import com.sstudio.madesubmissionmoviecatalogue.NetworkReceiver
import com.sstudio.madesubmissionmoviecatalogue.R
import com.sstudio.madesubmissionmoviecatalogue.adapter.CastAdapter
import com.sstudio.madesubmissionmoviecatalogue.adapter.GenreAdapter
import com.sstudio.madesubmissionmoviecatalogue.data.local.FavoriteDb
import com.sstudio.madesubmissionmoviecatalogue.model.CastResponse
import com.sstudio.madesubmissionmoviecatalogue.model.Detail
import com.sstudio.madesubmissionmoviecatalogue.model.MovieTv
import com.sstudio.madesubmissionmoviecatalogue.model.VideoResponse
import com.sstudio.madesubmissionmoviecatalogue.mvp.FavoriteAsyncCallback
import com.sstudio.madesubmissionmoviecatalogue.mvp.detail.presenter.DetailPresenter
import kotlinx.android.synthetic.main.activity_detail.*
import kotlinx.android.synthetic.main.content_detail.*
import kotlinx.android.synthetic.main.movie_wrapper.*
import java.lang.ref.WeakReference
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject


class DetailActivity : AppCompatActivity(), DetailView,
    FavoriteAsyncCallback {

    @Inject
    lateinit var detailPresenter: DetailPresenter
    private var myReceiver: BroadcastReceiver? = null
    private lateinit var myObserver: DataObserver
    private lateinit var handlerThread: HandlerThread
    private var uri: Uri? = null
    private var inc = 0

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
        initLayout()
        showMovieDetail()
        btn_favorite.setOnClickListener(favoriteOnClick)
        myReceiver = NetworkReceiver()
    }

    private val favoriteOnClick = View.OnClickListener {
        uri?.let { it1 -> detailPresenter.favoriteClick(movieTv, it1) }
    }

    private fun initHandler() {
        handlerThread = HandlerThread("DataObserver")
        handlerThread.start()
        val handler = Handler(handlerThread.looper)
        myObserver = DataObserver(handler, this)
        this.contentResolver.registerContentObserver(FavoriteDb.CONTENT_URI, true, myObserver)
    }

    private fun initLayout() {
        rv_genre.setHasFixedSize(true)
        val layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        rv_genre.layoutManager = layoutManager
        rv_genre.setHasFixedSize(true)
        rv_genre.adapter = GenreAdapter()

        rv_cast.setHasFixedSize(true)
        rv_cast.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        rv_cast.setHasFixedSize(true)
        rv_cast.adapter = CastAdapter()
    }

    private fun showMovieDetail() {
        movieTv = intent.getParcelableExtra<MovieTv>(EXTRA_DETAIL) as MovieTv


        if (movieTv.releaseDate != ""){
            movieTv.isMovie = 1
        } else {
            movieTv.isMovie = 0
        }

        detailPresenter.getMovieDetail(movieTv.id, movieTv.isMovie)
        detailPresenter.getMovieVideo(movieTv.id, movieTv.isMovie)
        detailPresenter.getMovieCredits(movieTv.id, movieTv.isMovie)
        uri = intent.data
        val uri = intent.data
        uri?.let { LoadFavoriteAsync(this).execute() }
        progressVisible()
        txt_rating.text = movieTv.voteAverage.toString()
        txt_votes.text = " / ${movieTv.voteCount}"
        Glide.with(this)
            .load(BuildConfig.POSTER_DETAIL + movieTv.posterPath)
            .thumbnail(0.5f)
            .skipMemoryCache(true)
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .placeholder(R.drawable.ic_cloud_download_grey_24dp)
            .into(img_poster)
        if (movieTv.isMovie == 1) {
            setToolbarTitle(movieTv.title)
            txt_release_date.text = displayDate(movieTv.releaseDate)
        } else {
            setToolbarTitle(movieTv.name)
            txt_release_date.text = displayDate(movieTv.firstAirDate)
        }
        img_poster.animation = AnimationUtils.loadAnimation(this, R.anim.scale_animation)
    }

    override fun showAdditionalDetails(
        detail: Detail?,
        castResponse: CastResponse?,
        videoResponse: VideoResponse?
    ) {
        detail?.let {
            txt_overview.text =
                if (it.overview != "") {
                    it.overview
                } else {
                    getString(R.string.msg_no_description)
                }
            (rv_genre.adapter as GenreAdapter).addGenre(it.genres)
            Glide.with(this)
                .load(BuildConfig.POSTER_DETAIL + it.backdropPath)
                .skipMemoryCache(true)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .fitCenter()
                .placeholder(R.drawable.ic_cloud_download_grey_24dp)
                .into(img_backdrop)
        }
        castResponse?.let {
            (rv_cast.adapter as CastAdapter).addCast(it.cast)
        }
        videoResponse?.let {response ->
            btn_play.setOnClickListener {
                if (response.video.isNotEmpty()) {
                    val intent = Intent(
                        Intent.ACTION_VIEW,
                        Uri.parse(BuildConfig.YOUTUBE + response.video[0].key)
                    )
                    startActivity(intent)
                } else {
                    toast(getString(R.string.msg_no_video))
                }
            }
        }
        inc ++
        if (inc >= 3) {
            progressGone()
            inc = 0
        }
    }

    private fun setupToolbar() {
        setSupportActionBar(appbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    private fun setToolbarTitle(title: String) {
        tv_title.text = title
        appbarLayout.addOnOffsetChangedListener(object : AppBarLayout.OnOffsetChangedListener {
            var isShow = true
            var scrollRange = -1

            override fun onOffsetChanged(appBarLayout: AppBarLayout, verticalOffset: Int) {
                if (scrollRange == -1) {
                    scrollRange = appBarLayout.totalScrollRange
                }
                if (scrollRange + verticalOffset == 0) {
                    collapsingToolbar.title = title
                    isShow = true
                } else if (isShow) {
                    collapsingToolbar.title = " "
                    isShow = false
                }
            }
        })
    }

    override fun isShowFavorite(resource: Int) {
        btn_favorite.setImageResource(resource)
    }


    override fun toast(text: String?) {
        Toast.makeText(this, "$text", Toast.LENGTH_SHORT).show()
    }

    override fun onDestroy() {
        super.onDestroy()
        Glide.get(this).clearMemory()
    }

    override fun onPause() {
        super.onPause()
        try {
            unregisterReceiver(myReceiver)
        } catch (e: IllegalArgumentException) {
            e.printStackTrace()
        }
    }

    private fun displayDate(date: String) : String{
        val cal = Calendar.getInstance()
        cal.time = SimpleDateFormat("yyyy-MM-dd", Locale(getString(R.string.language)))
            .parse(date) as Date
        val mDate = cal.get(Calendar.DATE).toString()
        val month = cal.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale(getString(R.string.language)))
        val year = cal.get(Calendar.YEAR).toString()

        return "$mDate $month $year"
    }

    private fun progressVisible() {
        loading_progress.visibility = View.VISIBLE
    }

    private fun progressGone() {
        loading_progress.visibility = View.GONE
    }

    override fun broadcastIntent() {
        try {
            registerReceiver(
                myReceiver,
                IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION)
            )
        } catch (e: IllegalArgumentException) {
            e.printStackTrace()
        }
    }

    override fun doInBackground(): Cursor? {
        return uri?.let { detailPresenter.loadFavoriteProvider(it) }
    }

    override fun onPostExecute(cursor: Cursor?) {
        cursor?.let { detailPresenter.setFavButton(it) }
    }

    internal class LoadFavoriteAsync(
        favoriteAsyncCallback: FavoriteAsyncCallback
    ) : AsyncTask<Void, Void, Cursor>() {

        private val weakCallback = WeakReference(favoriteAsyncCallback)

        override fun doInBackground(vararg p0: Void?): Cursor? {
            return weakCallback.get()?.doInBackground()
        }

        override fun onPostExecute(result: Cursor?) {
            super.onPostExecute(result)
            weakCallback.get()?.onPostExecute(result)
        }
    }

    class DataObserver(
        handler: Handler,
        private val context: Context
    ) : ContentObserver(handler) {

        override fun onChange(selfChange: Boolean) {
            super.onChange(selfChange)
            LoadFavoriteAsync(context as FavoriteAsyncCallback).execute()
        }
    }
}

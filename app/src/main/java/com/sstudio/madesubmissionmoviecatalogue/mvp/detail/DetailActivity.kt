package com.sstudio.madesubmissionmoviecatalogue.mvp.detail

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.database.ContentObserver
import android.database.Cursor
import android.net.ConnectivityManager
import android.net.Uri
import android.os.*
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.google.android.material.appbar.AppBarLayout
import com.sstudio.madesubmissionmoviecatalogue.*
import com.sstudio.madesubmissionmoviecatalogue.adapter.CastAdapter
import com.sstudio.madesubmissionmoviecatalogue.adapter.GenreAdapter
import com.sstudio.madesubmissionmoviecatalogue.adapter.SimilarAdapter
import com.sstudio.madesubmissionmoviecatalogue.adapter.VideoAdapter
import com.sstudio.madesubmissionmoviecatalogue.data.local.FavoriteDb
import com.sstudio.madesubmissionmoviecatalogue.model.*
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
    FavoriteAsyncCallback, SimilarAdapter.SimilarAdapterCallback,
    VideoAdapter.VideoAdapterCallback {

    @Inject
    lateinit var detailPresenter: DetailPresenter
    private var myReceiver: BroadcastReceiver? = null
    private lateinit var myObserver: DataObserver
    private lateinit var handlerThread: HandlerThread
    private lateinit var similarAdapter: SimilarAdapter
    private lateinit var videoAdapter: VideoAdapter
    private lateinit var actionBar: Toolbar
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
        showMovieDetail(null)
        btn_favorite.setOnClickListener(favoriteOnClick)
        myReceiver = NetworkReceiver()
        Common.navigateDetail = ArrayList<MovieTv>()
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

        appbar.setNavigationIcon(R.drawable.ic_baseline_arrow_back_24)
        appbar.setNavigationOnClickListener {
            onBackPressed()
        }
        rv_genre.setHasFixedSize(true)
        val layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        rv_genre.layoutManager = layoutManager
        rv_genre.adapter = GenreAdapter(false, null)

        rv_cast.setHasFixedSize(true)
        rv_cast.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        rv_cast.adapter = CastAdapter()
        rv_similar.setHasFixedSize(true)
        rv_similar.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        similarAdapter = SimilarAdapter(this)
        rv_similar.adapter = similarAdapter

        rv_trailer.setHasFixedSize(true)
        rv_trailer.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        videoAdapter = VideoAdapter(this)
        rv_trailer.adapter = videoAdapter
    }

    private fun showMovieDetail(movieTvSimilar: MovieTv?) {
        movieTv = movieTvSimilar ?: intent.getParcelableExtra<MovieTv>(EXTRA_DETAIL) as MovieTv

        if (movieTv.releaseDate != ""){
            movieTv.isMovie = 1
        } else {
            movieTv.isMovie = 0
        }

        detailPresenter.getMovieDetail(movieTv.id, movieTv.isMovie)
        detailPresenter.getMovieVideo(movieTv.id, movieTv.isMovie)
        detailPresenter.getMovieCredits(movieTv.id, movieTv.isMovie)
        detailPresenter.getMovieSimilar(movieTv.id, movieTv.isMovie)

        val uri = Uri.parse("${FavoriteDb.CONTENT_URI}/" + movieTv.id)
        this.uri = uri
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
        detail: Detail,
        castResponse: CastResponse,
        videoResponse: VideoResponse, similarResponse: MoviesResponse
    ) {
        detail.let {
            if (it.overview == "") {
                detailPresenter.getOverviewEN(movieTv.id, movieTv.isMovie)
            }else{
                txt_overview.text = it.overview
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
        castResponse.let {
            (rv_cast.adapter as CastAdapter).addCast(it.cast)
        }
        videoResponse.let { response ->
                if (response.video.isNotEmpty()) {
                    tv_trailer_label.visibility = View.VISIBLE
                    videoAdapter.addVideo(response.video)
                } else {
//                    toast(getString(R.string.msg_no_video))
                    tv_trailer_label.visibility = View.GONE
                }

        }

        if (similarResponse.movieTv.isNotEmpty()){
            tv_similar_label.visibility = View.VISIBLE
            similarAdapter.addSimilar(similarResponse.movieTv)
        }else{
            tv_similar_label.visibility = View.GONE
        }
        progressGone()
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
        val month = cal.getDisplayName(
            Calendar.MONTH,
            Calendar.LONG,
            Locale(getString(R.string.language))
        )
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

    override fun showOverviewEN(string: String) {
        detailPresenter.postTranslate(string)
    }

    override fun showTranslate(text: String) {
        Handler(Looper.getMainLooper()).post { txt_overview.text = text }
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

    override fun similarClicked(mMovieTv: MovieTv) {
        Common.navigateDetail.add(movieTv) //add current movie tv
        showMovieDetail(mMovieTv)
        nested_detail.parent.requestChildFocus(nested_detail, nested_detail);
        nested_detail.fullScroll(View.FOCUS_UP);
        appbarLayout.setExpanded(true);
    }

    override fun onBackPressed() {
        if (Common.navigateDetail.isNotEmpty()){
            val last = Common.navigateDetail.size - 1
            showMovieDetail(Common.navigateDetail[last])
            Common.navigateDetail.removeAt(last)
        }else{
            super.onBackPressed()
        }
    }

    override fun videoClicked(movieTv: Video) {
        val intent = Intent(
            Intent.ACTION_VIEW,
            Uri.parse(BuildConfig.YOUTUBE + movieTv.key)
        )
        startActivity(intent)
    }
}

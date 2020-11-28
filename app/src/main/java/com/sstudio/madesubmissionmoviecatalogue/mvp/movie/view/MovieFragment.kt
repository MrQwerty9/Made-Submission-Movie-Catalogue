package com.sstudio.madesubmissionmoviecatalogue.mvp.movie.view

import android.app.ActivityOptions
import android.app.SearchManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.database.ContentObserver
import android.database.Cursor
import android.net.ConnectivityManager
import android.net.Uri
import android.os.*
import android.view.*
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.sstudio.madesubmissionmoviecatalogue.App
import com.sstudio.madesubmissionmoviecatalogue.Common
import com.sstudio.madesubmissionmoviecatalogue.NetworkReceiver
import com.sstudio.madesubmissionmoviecatalogue.R
import com.sstudio.madesubmissionmoviecatalogue.adapter.MovieTvAdapter
import com.sstudio.madesubmissionmoviecatalogue.data.local.FavoriteDb
import com.sstudio.madesubmissionmoviecatalogue.model.Genres
import com.sstudio.madesubmissionmoviecatalogue.model.MovieTv
import com.sstudio.madesubmissionmoviecatalogue.model.MovieTvHome
import com.sstudio.madesubmissionmoviecatalogue.mvp.FavoriteAsyncCallback
import com.sstudio.madesubmissionmoviecatalogue.mvp.MainActivity
import com.sstudio.madesubmissionmoviecatalogue.mvp.MovieClickAnim
import com.sstudio.madesubmissionmoviecatalogue.mvp.detail.DetailActivity
import com.sstudio.madesubmissionmoviecatalogue.mvp.movie.presenter.MovieTvPresenter
import com.sstudio.madesubmissionmoviecatalogue.mvp.movie.presenter.MovieTvPresenterImpl
import kotlinx.android.synthetic.main.fragment_movie.view.*
import java.lang.ref.WeakReference
import javax.inject.Inject


class MovieFragment : Fragment(), MovieTvView,
    FavoriteAsyncCallback,
    MovieClickAnim {

    private lateinit var movieTvAdapter: MovieTvAdapter
    private lateinit var mView: View
    @Inject
    lateinit var movieTvPresenter: MovieTvPresenter
    private lateinit var viewModel: MovieTvPresenterImpl
    private var myReceiver: BroadcastReceiver? = null
    private lateinit var myObserver: DataObserver
    private lateinit var handlerThread: HandlerThread

    companion object {
        var isShowFavorite = false
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (context?.applicationContext as App).createMovieComponent(this).inject(this)
        initHandler()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setHasOptionsMenu(true)
        myReceiver = NetworkReceiver()
        mView = inflater.inflate(R.layout.fragment_movie, container, false)
        init()
        mView.swipe_refresh.setOnRefreshListener(movieRefresh)
        movieTvPresenter.init()
        return mView
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        val searchManager = activity?.getSystemService(Context.SEARCH_SERVICE) as SearchManager?
        if (searchManager != null && !isShowFavorite) {
            val searchView = menu.findItem(R.id.search).actionView as SearchView
            searchView.setSearchableInfo(searchManager.getSearchableInfo(activity?.componentName))
            searchView.queryHint = resources.getString(R.string.search)
            searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {

                override fun onQueryTextSubmit(query: String): Boolean {
                    progressVisible()
                    movieTvPresenter.findMovies(query)
                    return true
                }

                override fun onQueryTextChange(newText: String): Boolean {
                    if (newText.isEmpty()) {
                        progressVisible()
                        movieTvPresenter.loadMovie(MovieTvPresenterImpl.POPULAR, 1, Common.genreSelected.id, Common.regionSelected)
                    }
                    return true
                }
            })
        }
    }

    private fun init() {
        isShowFavorite =
            (parentFragment as FavoriteFragment?) != null
        progressVisible()
        activity?.let {
            movieTvAdapter = MovieTvAdapter(it, true, this)
        }
        setViewModel()
        mView.rv_list_movie.adapter = movieTvAdapter
        notifChanged()
    }

    private fun initHandler() {
        handlerThread = HandlerThread("DataObserver")
        handlerThread.start()
        val handler = Handler(handlerThread.looper)
        myObserver = DataObserver(handler, this)
        context?.let {
            it.contentResolver?.registerContentObserver(FavoriteDb.CONTENT_URI, true, myObserver)
        }
    }

    private fun setViewModel() {
        viewModel = ViewModelProviders.of(this)
            .get(MovieTvPresenterImpl::class.java)
        if (viewModel.movies == null && !isShowFavorite) {
            movieTvPresenter.loadMovie(MovieTvPresenterImpl.POPULAR, 1, Common.genreSelected.id, Common.regionSelected)
        } else if (viewModel.moviesFavorite == null && isShowFavorite) {
//            movieTvPresenter.loadFavorite(true)
            context?.let { LoadFavoriteAsync(this).execute() }

        } else if (isShowFavorite) {
            showMoviesTv(viewModel.moviesFavorite)
        } else {
            showMoviesTv(viewModel.movies)
        }
    }

    private var movieRefresh: SwipeRefreshLayout.OnRefreshListener =
        SwipeRefreshLayout.OnRefreshListener {
            mView.swipe_refresh.isRefreshing = false
            init()
            if (isShowFavorite) {
//                movieTvPresenter.loadFavorite(true)
                context?.let { LoadFavoriteAsync(this).execute() }
            } else {
                movieTvPresenter.loadMovie(MovieTvPresenterImpl.POPULAR, 1, Common.genreSelected.id, Common.regionSelected)
            }
        }

    override fun onMovieClick(movie: MovieTv, movieImageView: ImageView) {
        val intent = Intent(context, DetailActivity::class.java)
        val uri =
            Uri.parse("${FavoriteDb.CONTENT_URI}/" + movie.id)
        intent.data = uri
        intent.putExtra(DetailActivity.EXTRA_DETAIL, movie)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            val options = ActivityOptions.makeSceneTransitionAnimation(
                activity,
                movieImageView, "sharedName"
            )
            startActivity(intent, options.toBundle())
        } else {
            startActivity(intent)
        }
    }

    override fun showMoviesTvHome(moviesTv: List<MovieTvHome>?) {

    }

    override fun showMoviesTv(moviesTv: List<MovieTv>?) {

        moviesTv?.let {
            if (it.isEmpty()){
                toast(context?.getString(R.string.empty_text))
            }
            movieTvAdapter.movieTv = it as ArrayList<MovieTv>
            if (isShowFavorite) {
                viewModel.moviesFavorite = it
            } else {
                viewModel.movies = it
            }
        }
        notifChanged()
        progressGone()
    }

    override fun showGenreList(genreList: List<Genres.Genre>?) {

    }

    override fun failShowMoviesTv(text: String?) {
        progressGone()
        toast(text)
    }

    override fun spanCountGridLayout(i: Int) {
        mView.rv_list_movie.layoutManager = GridLayoutManager(context, i)
    }

    override fun broadcastIntent() {
        try {
            activity?.registerReceiver(
                myReceiver,
                IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION)
            )
        } catch (e: IllegalArgumentException) {
            e.printStackTrace()
        }
    }

    override fun onPause() {
        super.onPause()
        activity?.let {
            try {
                it.unregisterReceiver(myReceiver)
            } catch (e: IllegalArgumentException) {
                e.printStackTrace()
            }
        }
    }

    override fun onDetach() {
        super.onDetach()
        if (MainActivity.isResetViewModel) {
            setViewModel()
            viewModel.movies = null
            MainActivity.isResetViewModel = false
        }
    }

    private fun toast(text: String?) {
        text?.let {
            Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
        }
    }

    private fun notifChanged() {
        movieTvAdapter.notifyDataSetChanged()
    }

    internal fun progressVisible() {
        mView.loading_progress.visibility = View.VISIBLE
    }

    private fun progressGone() {
        mView.loading_progress.visibility = View.GONE
    }

    override fun doInBackground(): Cursor? {
        return movieTvPresenter.loadFavoriteProvider()
    }

    override fun onPostExecute(cursor: Cursor?) {
        if (isShowFavorite) {
            cursor?.let { movieTvPresenter.showFavoriteProvider(it, true) }
        }
    }

    internal class LoadFavoriteAsync internal constructor(
        favoriteAsyncCallback: FavoriteAsyncCallback
    ) : AsyncTask<Void, Void, Cursor>() {

        private val weakCallback = WeakReference(favoriteAsyncCallback)

        override fun doInBackground(vararg p0: Void?): Cursor? {
            return weakCallback.get()?.doInBackground()
        }

        override fun onPostExecute(result: Cursor?) {
            super.onPostExecute(result)
            result?.let { weakCallback.get()?.onPostExecute(result) }
        }
    }

    class DataObserver(handler: Handler, private val movieFragment: MovieFragment) :
        ContentObserver(handler) {

        override fun onChange(selfChange: Boolean) {
            super.onChange(selfChange)
            LoadFavoriteAsync(movieFragment as FavoriteAsyncCallback).execute()
        }
    }
}

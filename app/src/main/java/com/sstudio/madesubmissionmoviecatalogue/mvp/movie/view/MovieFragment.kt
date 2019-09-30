package com.sstudio.madesubmissionmoviecatalogue.mvp.movie.view

import android.content.BroadcastReceiver
import android.content.IntentFilter
import android.net.ConnectivityManager
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.sstudio.madesubmissionmoviecatalogue.App
import com.sstudio.madesubmissionmoviecatalogue.MyReceiver
import com.sstudio.madesubmissionmoviecatalogue.R
import com.sstudio.madesubmissionmoviecatalogue.adapter.MovieTvAdapter
import com.sstudio.madesubmissionmoviecatalogue.model.MovieTv
import com.sstudio.madesubmissionmoviecatalogue.mvp.movie.presenter.MovieTvPresenter
import com.sstudio.madesubmissionmoviecatalogue.mvp.movie.presenter.MovieTvPresenterImpl
import kotlinx.android.synthetic.main.fragment_movie.view.*
import javax.inject.Inject
import com.sstudio.madesubmissionmoviecatalogue.mvp.MainActivity
import android.app.SearchManager
import android.content.Context
import android.database.ContentObserver
import android.database.Cursor
import android.os.AsyncTask
import android.os.Handler
import android.os.HandlerThread
import android.util.Log
import androidx.appcompat.widget.SearchView
import com.sstudio.madesubmissionmoviecatalogue.LoadMoviesCallback
import com.sstudio.madesubmissionmoviecatalogue.data.local.FavoriteDb
import com.sstudio.madesubmissionmoviecatalogue.helper.MappingHelper
import java.lang.ref.WeakReference


class MovieFragment : Fragment(), MovieTvView {

    private lateinit var movieTvAdapter: MovieTvAdapter
    private lateinit var mView: View
    @Inject
    lateinit var movieTvPresenter: MovieTvPresenter
    private lateinit var viewModel: MovieTvPresenterImpl
    private var myReceiver: BroadcastReceiver? = null
    private var isShowFavorite = false
    private lateinit var myObserver: MovieFragment.DataObserver
    private lateinit var handlerThread: HandlerThread

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
        myReceiver = MyReceiver()
        mView = inflater.inflate(R.layout.fragment_movie, container, false)
        init()
        mView.swipe_refresh.setOnRefreshListener(movieRefresh)
        movieTvPresenter.init()
        return mView
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        val searchManager = activity?.getSystemService(Context.SEARCH_SERVICE) as SearchManager?


        if (searchManager != null) {
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
                        movieTvPresenter.loadMovie()
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
            movieTvAdapter = MovieTvAdapter(it, true)
        }
        setViewModel()
        mView.rv_list_movie.adapter = movieTvAdapter
        notifChanged()
    }

    private fun initHandler() {
        handlerThread = HandlerThread("DataObserver")
        handlerThread.start()
        val handler = Handler(handlerThread.looper)
        context?.let {
            myObserver = DataObserver(handler, movieTvPresenter)
            it.contentResolver?.registerContentObserver(FavoriteDb.CONTENT_URI, true, myObserver)
        }
    }

    private fun setViewModel() {
        viewModel = ViewModelProviders.of(this)
            .get(MovieTvPresenterImpl::class.java)
        if (viewModel.movies == null && !isShowFavorite) {
            movieTvPresenter.loadMovie()
        } else if (viewModel.moviesFavorite == null && isShowFavorite) {
//            movieTvPresenter.loadFavorite(true)
            context?.let { MovieFragment.LoadFavoriteAsync(movieTvPresenter).execute() }

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
                context?.let { MovieFragment.LoadFavoriteAsync(movieTvPresenter).execute() }
            } else {
                movieTvPresenter.loadMovie()
            }
        }

    override fun showMoviesTv(moviesTv: List<MovieTv>?) {
        moviesTv?.let {
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

    override fun onDestroy() {
        super.onDestroy()
        movieTvPresenter.dumpData()
    }

    internal class LoadFavoriteAsync internal constructor(
        movieTvPresenter: MovieTvPresenter
    ) : AsyncTask<Void, Void, Cursor>() {

        private val weakCallback = WeakReference(movieTvPresenter)

        override fun doInBackground(vararg p0: Void?): Cursor? {
            return weakCallback.get()?.loadFavoriteProvider()
        }

        override fun onPostExecute(result: Cursor?) {
            super.onPostExecute(result)
            result?.let { weakCallback.get()?.favoriteToListProvider(it, true) }
        }
    }

    private fun toast(text: String?) {
        Toast.makeText(context, text, Toast.LENGTH_SHORT).show()
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

    class DataObserver(handler: Handler, private val movieTvPresenter: MovieTvPresenter) : ContentObserver(handler) {

        override fun onChange(selfChange: Boolean) {
            super.onChange(selfChange)
            MovieFragment.LoadFavoriteAsync(movieTvPresenter).execute()

        }
    }
}

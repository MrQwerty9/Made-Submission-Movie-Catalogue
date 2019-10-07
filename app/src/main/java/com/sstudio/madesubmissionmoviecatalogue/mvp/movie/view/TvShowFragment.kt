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
import com.sstudio.madesubmissionmoviecatalogue.NetworkReceiver
import com.sstudio.madesubmissionmoviecatalogue.R
import com.sstudio.madesubmissionmoviecatalogue.adapter.MovieTvAdapter
import com.sstudio.madesubmissionmoviecatalogue.data.local.FavoriteDb.Companion.CONTENT_URI
import com.sstudio.madesubmissionmoviecatalogue.model.MovieTv
import com.sstudio.madesubmissionmoviecatalogue.mvp.FavoriteAsyncCallback
import com.sstudio.madesubmissionmoviecatalogue.mvp.MainActivity
import com.sstudio.madesubmissionmoviecatalogue.mvp.MovieClickAnim
import com.sstudio.madesubmissionmoviecatalogue.mvp.detail.DetailActivity
import com.sstudio.madesubmissionmoviecatalogue.mvp.movie.presenter.MovieTvPresenter
import com.sstudio.madesubmissionmoviecatalogue.mvp.movie.presenter.MovieTvPresenterImpl
import kotlinx.android.synthetic.main.fragment_tvshow.view.*
import java.lang.ref.WeakReference
import javax.inject.Inject

class TvShowFragment : Fragment(), MovieTvView,
    FavoriteAsyncCallback,
    MovieClickAnim {

    private lateinit var movieTvAdapter: MovieTvAdapter
    private lateinit var mView: View
    @Inject
    lateinit var movieTvPresenter: MovieTvPresenter
    private lateinit var viewModel: MovieTvPresenterImpl
    private var myReceiver: BroadcastReceiver? = null
    private var isShowFavorite = false
    private lateinit var myObserver: DataObserver
    private lateinit var handlerThread: HandlerThread

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (context?.applicationContext as App).createMovieComponent(this).inject(this)
        handlerThread = HandlerThread("DataObserver")
        handlerThread.start()
        val handler = Handler(handlerThread.looper)
        myObserver = DataObserver(handler, this)
        context?.let {
            it.contentResolver?.registerContentObserver(CONTENT_URI, true, myObserver)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setHasOptionsMenu(true)
        myReceiver = NetworkReceiver()
        mView = inflater.inflate(R.layout.fragment_tvshow, container, false)
        init()
        mView.swipe_refresh.setOnRefreshListener(tvRefresh)
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
                    movieTvPresenter.findTv(query)
                    return true
                }

                override fun onQueryTextChange(newText: String): Boolean {
                    if (newText.isEmpty()) {
                        progressVisible()
                        movieTvPresenter.loadTvShow()
                    }
                    return true
                }
            })
        }
    }

    private fun init() {
        isShowFavorite = (parentFragment as FavoriteFragment?) != null
        progressVisible()
        activity?.let {
            movieTvAdapter = MovieTvAdapter(it, false, this)
        }
        setViewModel()
        mView.rv_list_tv_show.adapter = movieTvAdapter
        notifChanged()
    }

    private fun setViewModel() {
        viewModel = ViewModelProviders.of(this)
            .get(MovieTvPresenterImpl::class.java)
        if (viewModel.tvShow == null && !isShowFavorite) {
            movieTvPresenter.loadTvShow()
        } else if (viewModel.tvShowFavorite == null && isShowFavorite) {
//            movieTvPresenter.loadFavorite(false)
            context?.let { LoadFavoriteAsync(this).execute() }
        } else if (isShowFavorite) {
            showMoviesTv(viewModel.tvShowFavorite)
        } else {
            showMoviesTv(viewModel.tvShow)
        }
    }

    private var tvRefresh: SwipeRefreshLayout.OnRefreshListener =
        SwipeRefreshLayout.OnRefreshListener {
            mView.swipe_refresh.isRefreshing = false
            init()
            if (isShowFavorite) {
//            movieTvPresenter.loadFavorite(false)
                context?.let { LoadFavoriteAsync(this).execute() }
            } else {
                movieTvPresenter.loadTvShow()
            }
        }

    override fun onMovieClick(movie: MovieTv, movieImageView: ImageView) {
        val intent = Intent(context, DetailActivity::class.java)
        val uri =
            Uri.parse("$CONTENT_URI/" + movie.id)
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

    override fun showMoviesTv(moviesTv: List<MovieTv>?) {
        moviesTv?.let {
            if (moviesTv.isEmpty()){
                toast(context?.getString(R.string.empty_text))
            }
            movieTvAdapter.movieTv = it as ArrayList<MovieTv>
            if (isShowFavorite) {
                viewModel.moviesFavorite = it
            } else {
                viewModel.tvShow = it
            }
        }
        notifChanged()
        progressGone()
    }

    override fun spanCountGridLayout(i: Int) {
        mView.rv_list_tv_show.layoutManager = GridLayoutManager(context, i)
    }

    override fun failShowMoviesTv(text: String?) {
        progressGone()
        toast(text)
    }

    override fun broadcastIntent() {
        progressGone()
        activity?.let {
            try {
                it.registerReceiver(
                    myReceiver,
                    IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION)
                )
            } catch (e: IllegalArgumentException) {
                e.printStackTrace()
            }
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
            viewModel.tvShow = null
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

    private fun progressVisible() {
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
            cursor?.let { movieTvPresenter.showFavoriteProvider(it, false) }
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

    class DataObserver(handler: Handler, private val tvShowFragment: TvShowFragment) :
        ContentObserver(handler) {

        override fun onChange(selfChange: Boolean) {
            super.onChange(selfChange)
            LoadFavoriteAsync(tvShowFragment as FavoriteAsyncCallback).execute()
        }
    }
}

package com.sstudio.madesubmissionmoviecatalogue.mvp.view

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
import com.sstudio.madesubmissionmoviecatalogue.*
import com.sstudio.madesubmissionmoviecatalogue.adapter.MovieTvAdapter
import com.sstudio.madesubmissionmoviecatalogue.data.local.FavoriteDb
import com.sstudio.madesubmissionmoviecatalogue.model.MovieTv
import com.sstudio.madesubmissionmoviecatalogue.mvp.FavoriteAsyncCallback
import com.sstudio.madesubmissionmoviecatalogue.mvp.presenter.MovieTvPresenter
import com.sstudio.madesubmissionmoviecatalogue.mvp.presenter.MovieTvPresenterImpl
import kotlinx.android.synthetic.main.fragment_movie.view.*
import java.lang.ref.WeakReference
import javax.inject.Inject


class MovieFragment : Fragment(), MovieTvView,
    FavoriteAsyncCallback{

    private lateinit var movieTvAdapter: MovieTvAdapter
    private lateinit var mView: View
    lateinit var movieTvPresenter: MovieTvPresenter
    private lateinit var viewModel: MovieTvPresenterImpl
    private lateinit var myObserver: DataObserver
    private lateinit var handlerThread: HandlerThread

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        (context?.applicationContext as App).createMovieComponent(this).inject(this)
        initHandler()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setHasOptionsMenu(true)
        mView = inflater.inflate(R.layout.fragment_movie, container, false)
        init()
        mView.swipe_refresh.setOnRefreshListener(movieRefresh)
        movieTvPresenter.init()
        return mView
    }

    private fun init() {
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
        myObserver = DataObserver(handler, this)
        context?.let {
            it.contentResolver?.registerContentObserver(FavoriteDb.CONTENT_URI, true, myObserver)
        }
    }

    private fun setViewModel() {
        viewModel = ViewModelProviders.of(this)
            .get(MovieTvPresenterImpl::class.java)
        if (viewModel.moviesFavorite == null) {
//            movieTvPresenter.loadFavorite(true)
            context?.let { LoadFavoriteAsync(this).execute() }
        } else {
            showMoviesTv(viewModel.moviesFavorite)
        }
    }

    private var movieRefresh: SwipeRefreshLayout.OnRefreshListener =
        SwipeRefreshLayout.OnRefreshListener {
            mView.swipe_refresh.isRefreshing = false
            init()
                context?.let { LoadFavoriteAsync(this).execute() }

        }

    override fun showMoviesTv(moviesTv: List<MovieTv>?) {

        moviesTv?.let {
            if (it.isEmpty()){
                toast(context?.getString(R.string.empty_text))
            }
            movieTvAdapter.movieTv = it as ArrayList<MovieTv>
                viewModel.moviesFavorite = it

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

    override fun onDetach() {
        super.onDetach()
//        if (MainActivity.isResetViewModel) {
//            setViewModel()
//            viewModel.movies = null
//            MainActivity.isResetViewModel = false
//        }
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

    override fun doInBackground(): Cursor? {
        return movieTvPresenter.loadFavoriteProvider()
    }

    override fun onPostExecute(cursor: Cursor?) {

            cursor?.let { movieTvPresenter.showFavoriteProvider(it, true) }

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

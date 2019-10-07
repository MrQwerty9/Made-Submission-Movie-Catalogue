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
import com.sstudio.madesubmissionmoviecatalogue.data.local.FavoriteDb.Companion.CONTENT_URI
import com.sstudio.madesubmissionmoviecatalogue.model.MovieTv
import com.sstudio.madesubmissionmoviecatalogue.mvp.FavoriteAsyncCallback
import com.sstudio.madesubmissionmoviecatalogue.mvp.presenter.MovieTvPresenter
import com.sstudio.madesubmissionmoviecatalogue.mvp.presenter.MovieTvPresenterImpl
import kotlinx.android.synthetic.main.fragment_tvshow.view.*
import java.lang.ref.WeakReference
import javax.inject.Inject

class TvShowFragment : Fragment(), MovieTvView,
    FavoriteAsyncCallback{

    private lateinit var movieTvAdapter: MovieTvAdapter
    private lateinit var mView: View
//    @Inject
    lateinit var movieTvPresenter: MovieTvPresenter
    private lateinit var viewModel: MovieTvPresenterImpl
    private lateinit var myObserver: DataObserver
    private lateinit var handlerThread: HandlerThread

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        (context?.applicationContext as App).createMovieComponent(this).inject(this)
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
        mView = inflater.inflate(R.layout.fragment_tvshow, container, false)
        init()
        mView.swipe_refresh.setOnRefreshListener(tvRefresh)
        movieTvPresenter.init()
        return mView
    }

    private fun init() {
        progressVisible()
        activity?.let {
            movieTvAdapter = MovieTvAdapter(it, false)
        }
        setViewModel()
        mView.rv_list_tv_show.adapter = movieTvAdapter
        notifChanged()
    }

    private fun setViewModel() {
        viewModel = ViewModelProviders.of(this)
            .get(MovieTvPresenterImpl::class.java)
       if (viewModel.tvShowFavorite == null) {
//            movieTvPresenter.loadFavorite(false)
            context?.let { LoadFavoriteAsync(this).execute() }
        } else{
            showMoviesTv(viewModel.tvShowFavorite)
        }
    }

    private var tvRefresh: SwipeRefreshLayout.OnRefreshListener =
        SwipeRefreshLayout.OnRefreshListener {
            mView.swipe_refresh.isRefreshing = false
            init()

//            movieTvPresenter.loadFavorite(false)
                context?.let { LoadFavoriteAsync(this).execute() }

        }

    override fun showMoviesTv(moviesTv: List<MovieTv>?) {
        moviesTv?.let {
            if (moviesTv.isEmpty()){
                toast(context?.getString(R.string.empty_text))
            }
            movieTvAdapter.movieTv = it as ArrayList<MovieTv>
                viewModel.moviesFavorite = it

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

    private fun toast(text: String?) {
        Toast.makeText(context, text, Toast.LENGTH_SHORT).show()
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
            cursor?.let { movieTvPresenter.showFavoriteProvider(it, false) }

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

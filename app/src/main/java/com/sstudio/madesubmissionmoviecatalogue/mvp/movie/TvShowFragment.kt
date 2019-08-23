package com.sstudio.madesubmissionmoviecatalogue.mvp.movie

import android.content.BroadcastReceiver
import android.content.IntentFilter
import android.net.ConnectivityManager
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.sstudio.madesubmissionmoviecatalogue.MyReceiver
import com.sstudio.madesubmissionmoviecatalogue.R
import com.sstudio.madesubmissionmoviecatalogue.adapter.MovieTvAdapter
import com.sstudio.madesubmissionmoviecatalogue.model.MovieTv
import kotlinx.android.synthetic.main.fragment_tvshow.view.*

class TvShowFragment : Fragment(), MovieTvView {

    private lateinit var movieTvAdapter: MovieTvAdapter
    private lateinit var mView: View
    private lateinit var movieTvPresenter: MovieTvPresenter
    private lateinit var viewModel: MovieTvPresenter
    private var myReceiver: BroadcastReceiver? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        myReceiver = MyReceiver()
        mView = inflater.inflate(R.layout.fragment_tvshow, container, false)

        init()
        mView.swipe_refresh.setOnRefreshListener(tvRefresh)
        movieTvPresenter.init()
        if (viewModel.movies == null) {
            movieTvPresenter.loadTvShow()
        } else {
            showMoviesTv(viewModel.movies)
        }
        return mView
    }

    private fun init(){
        mView.loading_progress.visibility = View.VISIBLE
        activity?.let {
            movieTvAdapter = MovieTvAdapter(it, false)
            movieTvPresenter = MovieTvPresenter(it, this)
        }
        viewModel = ViewModelProviders.of(this)
            .get(MovieTvPresenter::class.java)
        mView.rv_list_tv_show.adapter = movieTvAdapter
        movieTvAdapter.notifyDataSetChanged()
    }

    private var tvRefresh: SwipeRefreshLayout.OnRefreshListener = SwipeRefreshLayout.OnRefreshListener{
        mView.swipe_refresh.isRefreshing = false
        init()
        movieTvPresenter.loadTvShow()
    }

    override fun showMoviesTv(moviesTv: List<MovieTv>?) {
        moviesTv?.let {
            movieTvAdapter.movieTv = it
            viewModel.movies = it}
        movieTvAdapter.notifyDataSetChanged()
        mView.loading_progress.visibility = View.GONE
    }

    override fun spanCountGridLayout(i: Int) {
        mView.rv_list_tv_show.layoutManager = GridLayoutManager(context, i)
    }
    override fun broadcastIntent() {
        mView.loading_progress.visibility = View.GONE
        activity?.let {
            try {
                it.registerReceiver(myReceiver, IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION))
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
}

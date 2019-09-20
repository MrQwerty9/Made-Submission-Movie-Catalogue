package com.sstudio.madesubmissionmoviecatalogue.mvp.movie.view

import android.content.BroadcastReceiver
import android.content.IntentFilter
import android.net.ConnectivityManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
import com.sstudio.madesubmissionmoviecatalogue.mvp.MainActivity
import com.sstudio.madesubmissionmoviecatalogue.mvp.movie.presenter.MovieTvPresenter
import com.sstudio.madesubmissionmoviecatalogue.mvp.movie.presenter.MovieTvPresenterImpl
import kotlinx.android.synthetic.main.fragment_tvshow.view.*
import javax.inject.Inject

class TvShowFragment : Fragment(), MovieTvView {

    private lateinit var movieTvAdapter: MovieTvAdapter
    private lateinit var mView: View
    @Inject
    lateinit var movieTvPresenter: MovieTvPresenter
    private lateinit var viewModel: MovieTvPresenterImpl
    private var myReceiver: BroadcastReceiver? = null
    var isShowFavorite = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (context?.applicationContext as App).createMovieComponent(this).inject(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        myReceiver = MyReceiver()
        mView = inflater.inflate(R.layout.fragment_tvshow, container, false)
        init()
        mView.swipe_refresh.setOnRefreshListener(tvRefresh)
        movieTvPresenter.init()
        return mView
    }

    private fun init(){
        isShowFavorite = (parentFragment as FavoriteFragment?) != null
        progressVisible()
        activity?.let {
            movieTvAdapter = MovieTvAdapter(it, false)
        }
        setViewModel()
        mView.rv_list_tv_show.adapter = movieTvAdapter
        notifChanged()
    }

    private fun setViewModel(){
        viewModel = ViewModelProviders.of(this)
            .get(MovieTvPresenterImpl::class.java)
        if (viewModel.tvShow == null && !isShowFavorite) {
            movieTvPresenter.loadTvShow()
        }else if (viewModel.tvShowFavorite == null && isShowFavorite) {
            movieTvPresenter.loadFavorite(false)
        } else if (isShowFavorite){
            showMoviesTv(viewModel.tvShowFavorite)
        } else {
            showMoviesTv(viewModel.tvShow)
        }
    }

    private var tvRefresh: SwipeRefreshLayout.OnRefreshListener = SwipeRefreshLayout.OnRefreshListener{
        mView.swipe_refresh.isRefreshing = false
        init()
        if (isShowFavorite){
            movieTvPresenter.loadFavorite(false)
        }else {
            movieTvPresenter.loadTvShow()
        }
    }

    override fun showMoviesTv(moviesTv: List<MovieTv>?) {
        moviesTv?.let {
            movieTvAdapter.movieTv = it
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

    override fun onDetach() {
        super.onDetach()
        if (MainActivity.isResetViewModel) {
            setViewModel()
            viewModel.tvShow = null
            MainActivity.isResetViewModel = false
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        movieTvPresenter.dumpData()
    }

    private fun toast(text: String?){
        Toast.makeText(context, text, Toast.LENGTH_SHORT).show()
    }

    private fun notifChanged(){
        movieTvAdapter.notifyDataSetChanged()
    }

    private fun progressVisible(){
        mView.loading_progress.visibility = View.VISIBLE
    }

    private fun progressGone(){
        mView.loading_progress.visibility = View.GONE
    }
}

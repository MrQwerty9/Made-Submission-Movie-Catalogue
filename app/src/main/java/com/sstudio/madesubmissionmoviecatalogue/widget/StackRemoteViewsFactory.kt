package com.sstudio.madesubmissionmoviecatalogue.widget

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.widget.RemoteViews
import android.widget.RemoteViewsService
import androidx.core.net.toUri
import com.bumptech.glide.Glide
import com.bumptech.glide.request.FutureTarget
import com.sstudio.madesubmissionmoviecatalogue.App
import com.sstudio.madesubmissionmoviecatalogue.BuildConfig
import com.sstudio.madesubmissionmoviecatalogue.R
import com.sstudio.madesubmissionmoviecatalogue.model.MovieTv
import com.sstudio.madesubmissionmoviecatalogue.mvp.detail.presenter.FavoriteInteractor
import com.sstudio.madesubmissionmoviecatalogue.widget.FavoriteWidget.Companion.EXTRA_FIRST_AIR
import com.sstudio.madesubmissionmoviecatalogue.widget.FavoriteWidget.Companion.EXTRA_GENRE
import com.sstudio.madesubmissionmoviecatalogue.widget.FavoriteWidget.Companion.EXTRA_ID
import com.sstudio.madesubmissionmoviecatalogue.widget.FavoriteWidget.Companion.EXTRA_ISMOVIE
import com.sstudio.madesubmissionmoviecatalogue.widget.FavoriteWidget.Companion.EXTRA_NAME
import com.sstudio.madesubmissionmoviecatalogue.widget.FavoriteWidget.Companion.EXTRA_OVERVIEW
import com.sstudio.madesubmissionmoviecatalogue.widget.FavoriteWidget.Companion.EXTRA_POSTER
import com.sstudio.madesubmissionmoviecatalogue.widget.FavoriteWidget.Companion.EXTRA_RELEASE
import com.sstudio.madesubmissionmoviecatalogue.widget.FavoriteWidget.Companion.EXTRA_TITLE
import com.sstudio.madesubmissionmoviecatalogue.widget.FavoriteWidget.Companion.EXTRA_VOTE_AVR
import com.sstudio.madesubmissionmoviecatalogue.widget.FavoriteWidget.Companion.EXTRA_VOTE_CNT
import javax.inject.Inject


class StackRemoteViewsFactory(private val context: Context) :
    RemoteViewsService.RemoteViewsFactory {

    @Inject
    lateinit var favoriteInteractor: FavoriteInteractor

    private var items: MutableList<Bitmap> = ArrayList()
    private var movies: List<MovieTv> = ArrayList()

    override fun onCreate() {
        (context.applicationContext as App).createInteractorComponent().inject(this)
    }

    override fun getLoadingView(): RemoteViews? {
        return null
    }

    override fun getItemId(p0: Int): Long {
        return 0
    }

    override fun onDataSetChanged() {
        movies = favoriteInteractor.getFavoriteSync()
        setBitmap(movies)
    }

    override fun hasStableIds(): Boolean {
        return false
    }

    override fun getViewAt(position: Int): RemoteViews {
        val remoteViews = RemoteViews(context.packageName,
            R.layout.widget_item
        )
        val bundle = Bundle()
        if (items.size > 0) {
            remoteViews.setImageViewBitmap(R.id.imageView, items[position])
            putData(bundle, position)
        }
        val fillIntent = Intent()
        fillIntent.putExtras(bundle)
        remoteViews.setOnClickFillInIntent(R.id.imageView, fillIntent)
        return remoteViews
    }

    override fun getCount(): Int {
        return items.size
    }

    override fun getViewTypeCount(): Int {
        return 1
    }

    override fun onDestroy() {

    }

    private fun putData(bundle: Bundle, position: Int) {
        bundle.putString(EXTRA_POSTER, movies[position].posterPath)
        bundle.putString(EXTRA_OVERVIEW, movies[position].overview)
        bundle.putString(EXTRA_RELEASE, movies[position].releaseDate)
        bundle.putInt(EXTRA_ID, movies[position].id)
        bundle.putString(EXTRA_TITLE, movies[position].title)
        bundle.putDouble(EXTRA_VOTE_AVR, movies[position].voteAverage)
        bundle.putInt(EXTRA_VOTE_CNT, movies[position].voteCount)
        bundle.putString(EXTRA_NAME, movies[position].name)
        bundle.putString(EXTRA_FIRST_AIR, movies[position].firstAirDate)
        bundle.putString(EXTRA_GENRE, movies[position].genre)
        bundle.putInt(EXTRA_ISMOVIE, movies[position].isMovie)
    }

    private fun setBitmap(list: List<MovieTv>) {
        items.removeAll(items)
        items.clear()

        for (i in 0 until list.size) {
            val futureBitmap: FutureTarget<Bitmap>  = Glide.with(context)
                .asBitmap()
                .load((BuildConfig.POSTER_DETAIL + list[i].posterPath).toUri())
                .submit()
            items.add(futureBitmap.get())
        }
    }
}
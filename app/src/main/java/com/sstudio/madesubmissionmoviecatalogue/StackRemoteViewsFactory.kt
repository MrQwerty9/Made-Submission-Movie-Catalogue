package com.sstudio.madesubmissionmoviecatalogue

import android.content.Context
import android.widget.RemoteViews
import android.widget.RemoteViewsService
import android.graphics.Bitmap
import android.content.Intent
import android.os.Bundle
import androidx.core.net.toUri
import com.sstudio.madesubmissionmoviecatalogue.model.MovieTv
import com.sstudio.madesubmissionmoviecatalogue.mvp.detail.presenter.FavoriteInteractor
import javax.inject.Inject
import kotlin.collections.ArrayList
import com.bumptech.glide.request.FutureTarget
import com.bumptech.glide.Glide


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
        val remoteViews = RemoteViews(context.packageName, R.layout.widget_item)
        remoteViews.setImageViewBitmap(R.id.imageView, items[position])

        val bundle = Bundle()
        bundle.putInt(FavoriteWidget.EXTRA_ITEM, position)

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

    private fun setBitmap(list: List<MovieTv>) {
        items.removeAll(items)
        items.clear()

        for (i in 0 until list.size) {
            val futureBitmap: FutureTarget<Bitmap>  = Glide.with(context)
                .asBitmap()
                .load((BuildConfig.POSTER + list[i].posterPath).toUri())
                .submit()
            items.add(futureBitmap.get())
        }
    }
}
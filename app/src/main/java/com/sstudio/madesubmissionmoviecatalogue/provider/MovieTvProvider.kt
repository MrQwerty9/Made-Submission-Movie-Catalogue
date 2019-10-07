package com.sstudio.madesubmissionmoviecatalogue.provider

import android.appwidget.AppWidgetManager
import android.content.*
import android.database.Cursor
import android.net.Uri
import android.os.Handler
import android.widget.Toast
import com.sstudio.madesubmissionmoviecatalogue.App
import com.sstudio.madesubmissionmoviecatalogue.widget.FavoriteWidget
import com.sstudio.madesubmissionmoviecatalogue.R
import com.sstudio.madesubmissionmoviecatalogue.data.local.FavoriteDb.Companion.AUTHORITY
import com.sstudio.madesubmissionmoviecatalogue.data.local.FavoriteDb.Companion.CONTENT_URI
import com.sstudio.madesubmissionmoviecatalogue.model.MovieTv
import com.sstudio.madesubmissionmoviecatalogue.model.MovieTv.Companion.TABLE_NAME
import com.sstudio.madesubmissionmoviecatalogue.mvp.detail.DetailActivity
import com.sstudio.madesubmissionmoviecatalogue.mvp.detail.presenter.FavoriteInteractor
import io.reactivex.Completable
import io.reactivex.CompletableObserver
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class MovieTvProvider : ContentProvider() {

    @Inject
    lateinit var favoriteInteractor: FavoriteInteractor
    private val NOTE = 1
    private val NOTE_ID = 2
    private val sUriMatcher = UriMatcher(UriMatcher.NO_MATCH)

    init {
        sUriMatcher.addURI(AUTHORITY, TABLE_NAME, NOTE)
        sUriMatcher.addURI(AUTHORITY, "$TABLE_NAME/#", NOTE_ID)
    }

    override fun insert(uri: Uri, values: ContentValues?): Uri? {
        var added: Long = 0
        when (sUriMatcher.match(uri)) {
            NOTE -> {
                val context = context ?: return null
                Completable.fromAction {
                    added = favoriteInteractor.insertFavorite(MovieTv.fromContentValues(values))
                }.observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io())
                    .subscribe(object :
                        CompletableObserver {
                        override fun onComplete() {
                            context.contentResolver.notifyChange(
                                CONTENT_URI, DetailActivity.DataObserver(
                                    Handler(), context
                                )
                            )
                            showToast(context.getString(R.string.favorite_added))
                            widgetNotifChanged()
                        }

                        override fun onSubscribe(d: Disposable) {

                        }

                        override fun onError(e: Throwable) {
                            showToast(context.getString(R.string.error_data) + e.message)
                        }

                    })

            }
            else -> added = 0
        }

        return ContentUris.withAppendedId(uri, added)
    }

    override fun query(
        uri: Uri,
        projection: Array<String>?,
        selection: String?,
        selectionArgs: Array<String>?,
        sortOrder: String?
    ): Cursor? {
        initInject()

        return if (sUriMatcher.match(uri) == NOTE || sUriMatcher.match(uri) == NOTE_ID) {
            val cursor: Cursor = if (sUriMatcher.match(uri) == NOTE) {
                favoriteInteractor.getFavoriteCursor()
            } else {
                favoriteInteractor.getFavByIdCursor(ContentUris.parseId(uri).toInt())
            }
            cursor
        } else {
            null
        }
    }

    override fun onCreate(): Boolean {
        return true
    }

    override fun update(p0: Uri, p1: ContentValues?, p2: String?, p3: Array<out String>?): Int {
        return 0
    }

    override fun delete(uri: Uri, p1: String?, p2: Array<out String>?): Int {
        var deleted: Int? = 0
        when (sUriMatcher.match(uri)) {
            NOTE_ID -> {
                val context = context ?: return 0
                Completable.fromAction {
                    deleted = favoriteInteractor.removeFavorite(ContentUris.parseId(uri).toInt())
                }.observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io())
                    .subscribe(object :
                        CompletableObserver {
                        override fun onComplete() {
                            context.contentResolver.notifyChange(
                                CONTENT_URI, DetailActivity.DataObserver(
                                    Handler(), context
                                )
                            )
                            showToast(context.getString(R.string.favorite_removed))
                            widgetNotifChanged()
                        }

                        override fun onSubscribe(d: Disposable) {

                        }

                        override fun onError(e: Throwable) {
                            showToast(context.getString(R.string.error_data) + e.message)
                        }

                    })
            }
            else -> deleted = 0
        }

        return deleted as Int
    }

    override fun getType(p0: Uri): String? {
        return null
    }

    private fun initInject() {
        (context?.applicationContext as App).createInteractorComponent().inject(this)
    }

    private fun widgetNotifChanged() {
        context?.let {
            val appWidgetManager = AppWidgetManager.getInstance(it)
            val component = ComponentName(it, FavoriteWidget::class.java)
            val appWidgetId = appWidgetManager.getAppWidgetIds(component)
            appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetId, R.id.stack_view)
        }
    }

    private fun showToast(text: String?) {
        Toast.makeText(context, "$text", Toast.LENGTH_SHORT).show()
    }

}
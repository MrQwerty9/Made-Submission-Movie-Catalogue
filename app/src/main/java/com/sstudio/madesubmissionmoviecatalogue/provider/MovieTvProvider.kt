package com.sstudio.madesubmissionmoviecatalogue.provider

import android.content.*
import android.database.Cursor
import android.database.Observable
import android.net.Uri
import android.os.Handler
import android.util.Log
import com.sstudio.madesubmissionmoviecatalogue.App
import com.sstudio.madesubmissionmoviecatalogue.data.local.FavoriteDb.Companion.AUTHORITY
import com.sstudio.madesubmissionmoviecatalogue.data.local.FavoriteDb.Companion.CONTENT_URI
import com.sstudio.madesubmissionmoviecatalogue.model.MovieTv
import com.sstudio.madesubmissionmoviecatalogue.model.MovieTv.Companion.TABLE_NAME
import com.sstudio.madesubmissionmoviecatalogue.mvp.MainActivity
import com.sstudio.madesubmissionmoviecatalogue.mvp.detail.DetailActivity
import com.sstudio.madesubmissionmoviecatalogue.mvp.detail.presenter.FavoriteInteractor
import javax.inject.Inject

class MovieTvProvider : ContentProvider() {

    @Inject
    lateinit var favoriteInteractor: FavoriteInteractor
//    private val uriMatcher: UriMatcher = UriMatcher(UriMatcher.NO_MATCH)
    private val NOTE = 1
    private val NOTE_ID = 2
    private val sUriMatcher = UriMatcher(UriMatcher.NO_MATCH)

    init {
        // content://com.dicoding.picodiploma.mynotesapp/note
        sUriMatcher.addURI(AUTHORITY, TABLE_NAME, NOTE);

        // content://com.dicoding.picodiploma.mynotesapp/note/id
        sUriMatcher.addURI(AUTHORITY, "$TABLE_NAME/#", NOTE_ID);
    }

    override fun insert(uri: Uri, values: ContentValues?): Uri? {
        val added= when (sUriMatcher.match(uri)) {
            NOTE -> {
                val context = context ?: return null
                favoriteInteractor.insertFavorite(MovieTv.fromContentValues(values))
            } else -> 0
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

        if (sUriMatcher.match(uri) == NOTE || sUriMatcher.match(uri) == NOTE_ID) {
            val context = context ?: return null
            val cursor: Cursor
            if (sUriMatcher.match(uri) == NOTE) {
                cursor = favoriteInteractor.getFavoriteCursor()
            } else {
                cursor = favoriteInteractor.getFavByIdCursor(ContentUris.parseId(uri).toInt())
            }
//            context.contentResolver.notifyChange(CONTENT_URI, DetailActivity.DataObserver(
//                Handler(), context, true))
            return cursor
        } else {
            throw IllegalArgumentException("Unknown URI: $uri")
        }
    }

    override fun onCreate(): Boolean {

//        uriMatcher.addURI(AUTHORITY, "movie", NOTE)
        return true
    }

    override fun update(p0: Uri, p1: ContentValues?, p2: String?, p3: Array<out String>?): Int {
        return 0
    }

    override fun delete(uri: Uri, p1: String?, p2: Array<out String>?): Int {
        val deleted: Int? = when (sUriMatcher.match(uri)) {
            NOTE_ID -> {
                val context = context ?: return 0
                favoriteInteractor.removeFavorite(ContentUris.parseId(uri).toInt())
            }
            else -> 0
        }

        return deleted as Int
    }

    override fun getType(p0: Uri): String? {
        return null
    }

    private fun initInject(){
        (context?.applicationContext as App).createInteractorComponent().inject(this)
    }

}
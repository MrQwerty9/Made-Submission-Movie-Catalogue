package com.sstudio.madesubmissionmoviecatalogue.provider

import android.content.*
import android.database.Cursor
import android.net.Uri
import com.sstudio.madesubmissionmoviecatalogue.App
import com.sstudio.madesubmissionmoviecatalogue.data.local.FavoriteDb
import com.sstudio.madesubmissionmoviecatalogue.data.local.FavoriteDb.Companion.AUTHORITY
import com.sstudio.madesubmissionmoviecatalogue.model.MovieTv
import com.sstudio.madesubmissionmoviecatalogue.model.MovieTv.Companion.TABLE_NAME
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
        when (sUriMatcher.match(uri)) {
            NOTE -> {
                val context = context ?: return null
                val id = FavoriteDb.getInstance(context).favoriteDao()
                    .insert(MovieTv.fromContentValues(values))
                context.contentResolver.notifyChange(uri, null)
                return ContentUris.withAppendedId(uri, id)
            }
            NOTE_ID -> throw IllegalArgumentException("Invalid URI, cannot insert with ID: $uri")
            else -> throw IllegalArgumentException("Unknown URI: $uri")
        }
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
            cursor.setNotificationUri(context.contentResolver, uri)
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

    override fun delete(p0: Uri, p1: String?, p2: Array<out String>?): Int {
        return 0
    }

    override fun getType(p0: Uri): String? {
        return null
    }

    private fun initInject(){
        (context?.applicationContext as App).createInteractorComponent().inject(this)
    }

}
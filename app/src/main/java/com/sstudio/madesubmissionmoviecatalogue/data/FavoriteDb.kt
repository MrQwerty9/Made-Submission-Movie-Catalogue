package com.sstudio.madesubmissionmoviecatalogue.data

import android.net.Uri
import com.sstudio.madesubmissionmoviecatalogue.model.MovieTv.Companion.TABLE_NAME

abstract class FavoriteDb {

    companion object {
        const val AUTHORITY = "com.sstudio.madesubmissionmoviecatalogue"
        private val SCHEME = "content"
        val CONTENT_URI = Uri.Builder().scheme(SCHEME)
            .authority(AUTHORITY)
            .appendPath(TABLE_NAME)
            .build()
    }
}

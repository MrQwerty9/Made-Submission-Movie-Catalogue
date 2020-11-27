package com.sstudio.madesubmissionmoviecatalogue.data.local

import android.net.Uri
import androidx.room.Database
import androidx.room.RoomDatabase
import com.sstudio.madesubmissionmoviecatalogue.model.MovieTv
import com.sstudio.madesubmissionmoviecatalogue.model.MovieTv.Companion.TABLE_NAME

@Database(entities = [(MovieTv::class)], version = FavoriteDb.DB_VERSION, exportSchema = false)
abstract class FavoriteDb : RoomDatabase() {

    companion object {
        const val DB_VERSION = 1
        const val DB_NAME = "favoriteDb"
        const val AUTHORITY = "com.sstudio.madesubmissionmoviecatalogue"
        private val SCHEME = "content"
        val CONTENT_URI = Uri.Builder().scheme(SCHEME)
            .authority(AUTHORITY)
            .appendPath(TABLE_NAME)
            .build()
    }

    abstract fun favoriteDao(): FavoriteDao


}

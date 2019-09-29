package com.sstudio.madesubmissionmoviecatalogue.data.local

import android.content.Context
import android.net.Uri
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.sstudio.madesubmissionmoviecatalogue.model.MovieTv
import com.sstudio.madesubmissionmoviecatalogue.model.MovieTv.Companion.TABLE_NAME

@Database(entities = [(MovieTv::class)], version = FavoriteDb.DB_VERSION, exportSchema = false)
abstract class FavoriteDb : RoomDatabase() {

    companion object {
        const val DB_VERSION = 2
        const val DB_NAME = "favoriteDb"
        const val AUTHORITY = "com.sstudio.madesubmissionmoviecatalogue"
        const val TABLE_NOTE = "note"
        var sInstance: FavoriteDb? = null
        private val SCHEME = "content"
        val CONTENT_URI = Uri.Builder().scheme(SCHEME)
            .authority(AUTHORITY)
            .appendPath(TABLE_NAME)
            .build()
        @Synchronized
        fun getInstance(context: Context): FavoriteDb {

            if (sInstance == null) {
                sInstance = Room
                    .databaseBuilder(
                        context.applicationContext,
                        FavoriteDb::class.java,
                        "ex"
                    )
                    .build()
            }
            return sInstance as FavoriteDb
        }
    }

    abstract fun favoriteDao(): FavoriteDao


}

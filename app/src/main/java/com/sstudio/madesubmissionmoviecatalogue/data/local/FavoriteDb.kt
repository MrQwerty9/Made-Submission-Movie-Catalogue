package com.sstudio.madesubmissionmoviecatalogue.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.sstudio.madesubmissionmoviecatalogue.model.MovieTv

@Database(entities = [(MovieTv::class)], version = FavoriteDb.DB_VERSION, exportSchema = false)
abstract class FavoriteDb : RoomDatabase() {

    companion object {
        const val DB_VERSION = 2
        const val DB_NAME = "favoriteDb"
    }
    abstract fun favoriteDao(): FavoriteDao
}

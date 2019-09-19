package com.sstudio.madesubmissionmoviecatalogue.data.local

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class RoomModule {

    @Provides
    @Singleton
    fun provideFavoriteDb(context: Context): FavoriteDb {
        return Room.databaseBuilder(
            context,
            FavoriteDb::class.java,
            FavoriteDb.DB_NAME
        ).build()
    }

    @Provides
    @Singleton
    fun provideFavoriteDao(favoriteDb: FavoriteDb): FavoriteDao {
        return favoriteDb.favoriteDao()
    }
}
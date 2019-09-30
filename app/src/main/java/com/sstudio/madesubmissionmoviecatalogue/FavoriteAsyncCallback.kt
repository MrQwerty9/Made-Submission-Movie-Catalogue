package com.sstudio.madesubmissionmoviecatalogue

import android.database.Cursor

interface FavoriteAsyncCallback {
    fun doInBackground(isLoadFavorite: Boolean): Cursor?
    fun onPostExecute(cursor: Cursor?, isLoadFavorite: Boolean)
}
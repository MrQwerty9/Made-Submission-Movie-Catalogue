package com.sstudio.madesubmissionmoviecatalogue.mvp

import android.database.Cursor

interface FavoriteAsyncCallback {
    fun doInBackground(): Cursor?
    fun onPostExecute(cursor: Cursor?)
}
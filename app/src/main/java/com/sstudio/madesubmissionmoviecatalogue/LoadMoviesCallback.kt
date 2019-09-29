package com.sstudio.madesubmissionmoviecatalogue

import android.database.Cursor

interface LoadMoviesCallback {
    fun preExecute()
    fun postExecute(notes: Cursor)
}
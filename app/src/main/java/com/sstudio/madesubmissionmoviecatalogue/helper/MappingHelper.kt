package com.sstudio.madesubmissionmoviecatalogue.helper

import android.database.Cursor
import android.provider.BaseColumns._ID
import android.util.Log
import com.sstudio.madesubmissionmoviecatalogue.model.MovieTv
import com.sstudio.madesubmissionmoviecatalogue.model.MovieTv.Companion.COLUMN_FIRSTAIRDATE
import com.sstudio.madesubmissionmoviecatalogue.model.MovieTv.Companion.COLUMN_GENRE
import com.sstudio.madesubmissionmoviecatalogue.model.MovieTv.Companion.COLUMN_ID
import com.sstudio.madesubmissionmoviecatalogue.model.MovieTv.Companion.COLUMN_ISMOVIE
import com.sstudio.madesubmissionmoviecatalogue.model.MovieTv.Companion.COLUMN_NAME
import com.sstudio.madesubmissionmoviecatalogue.model.MovieTv.Companion.COLUMN_OVERVIEW
import com.sstudio.madesubmissionmoviecatalogue.model.MovieTv.Companion.COLUMN_POSTERPATH
import com.sstudio.madesubmissionmoviecatalogue.model.MovieTv.Companion.COLUMN_RELEASEDATE
import com.sstudio.madesubmissionmoviecatalogue.model.MovieTv.Companion.COLUMN_TITLE
import com.sstudio.madesubmissionmoviecatalogue.model.MovieTv.Companion.COLUMN_VOTEAVERAGE
import com.sstudio.madesubmissionmoviecatalogue.model.MovieTv.Companion.COLUMN_VOTECOUNT

class MappingHelper {
    companion object {
        fun mapCursorToArrayList(notesCursor: Cursor): ArrayList<MovieTv> {
            val notesList = ArrayList<MovieTv>()

            while (notesCursor.moveToNext()) {
                val posterPath =
                    notesCursor.getString(notesCursor.getColumnIndexOrThrow(COLUMN_POSTERPATH))
                val overview =
                    notesCursor.getString(notesCursor.getColumnIndexOrThrow(COLUMN_OVERVIEW))
                val releaseDate =
                    notesCursor.getString(notesCursor.getColumnIndexOrThrow(COLUMN_RELEASEDATE))
                val id = notesCursor.getInt(notesCursor.getColumnIndexOrThrow(COLUMN_ID))
                val title = notesCursor.getString(notesCursor.getColumnIndexOrThrow(COLUMN_TITLE))
                val voteAverage =
                    notesCursor.getDouble(notesCursor.getColumnIndexOrThrow(COLUMN_VOTEAVERAGE))
                val voteCount =
                    notesCursor.getInt(notesCursor.getColumnIndexOrThrow(COLUMN_VOTECOUNT))
                val name = notesCursor.getString(notesCursor.getColumnIndexOrThrow(COLUMN_NAME))
                val firstAirDate =
                    notesCursor.getString(notesCursor.getColumnIndexOrThrow(COLUMN_FIRSTAIRDATE))
                val genre = notesCursor.getString(notesCursor.getColumnIndexOrThrow(COLUMN_GENRE))
                val isMovie =
                    notesCursor.getInt(notesCursor.getColumnIndexOrThrow(COLUMN_ISMOVIE))
                notesList.add(MovieTv(posterPath, overview, releaseDate, id, title, voteAverage, voteCount, name, firstAirDate, genre, isMovie))
            }
            return notesList
        }
    }
}
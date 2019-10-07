package com.sstudio.madesubmissionmoviecatalogue.mvp

import android.widget.ImageView
import com.sstudio.madesubmissionmoviecatalogue.model.MovieTv

interface MovieClickAnim {
        fun onMovieClick(
            movie: MovieTv,
            movieImageView: ImageView
        )
}
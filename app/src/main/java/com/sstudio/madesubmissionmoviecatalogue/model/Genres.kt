package com.sstudio.madesubmissionmoviecatalogue.model

data class Genres (
    val genres: List<Genre>
) {
    data class Genre(
        var id: Int? = null,
        var name: String = ""
    )
}
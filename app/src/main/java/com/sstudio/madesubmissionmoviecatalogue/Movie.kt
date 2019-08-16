package com.sstudio.madesubmissionmoviecatalogue

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
class Movie (
    var title: String,
    var overview: String,
    var poster: Int
):Parcelable
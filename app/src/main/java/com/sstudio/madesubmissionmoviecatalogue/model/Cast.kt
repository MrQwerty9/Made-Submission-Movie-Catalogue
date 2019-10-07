package com.sstudio.madesubmissionmoviecatalogue.model

import com.google.gson.annotations.SerializedName

data class Cast (
    val name: String = "",
    @SerializedName("profile_path")
    val profilPath: String = ""
)
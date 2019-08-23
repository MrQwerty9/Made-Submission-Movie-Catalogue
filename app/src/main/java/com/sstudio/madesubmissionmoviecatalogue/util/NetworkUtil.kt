package com.sstudio.madesubmissionmoviecatalogue.util

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo
import androidx.core.content.ContextCompat.getSystemService
import com.sstudio.madesubmissionmoviecatalogue.R


object NetworkUtil {
    fun getConnectivityStatusString(context: Context): String? {
        var status: String? = null
        val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork = cm.activeNetworkInfo
        if (activeNetwork != null) {
            if (activeNetwork.type == ConnectivityManager.TYPE_WIFI) {
                status = context.resources.getString(R.string.wifi_enable)
                return status
            } else if (activeNetwork.type == ConnectivityManager.TYPE_MOBILE) {
                status = context.resources.getString(R.string.data_enable)
                return status
            }
        } else {
            status = context.resources.getString(R.string.no_internet)
            return status
        }
        return status
    }
}
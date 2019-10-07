package com.sstudio.madesubmissionmoviecatalogue

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.widget.Toast
import com.sstudio.madesubmissionmoviecatalogue.util.NetworkUtil

class NetworkReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        var status = NetworkUtil.getConnectivityStatusString(context)
        status?.let {
            if (it.isEmpty()) {
                status = context.resources.getString(R.string.no_internet)
            }
        }
        Toast.makeText(context, status, Toast.LENGTH_LONG).show()
    }

}

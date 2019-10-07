package com.sstudio.madesubmissionmoviecatalogue.widget

import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.widget.RemoteViews
import android.app.PendingIntent
import android.content.Intent
import android.net.Uri
import android.os.Parcelable
import android.util.Log
import android.widget.Toast
import com.sstudio.madesubmissionmoviecatalogue.R
import com.sstudio.madesubmissionmoviecatalogue.model.MovieTv
import com.sstudio.madesubmissionmoviecatalogue.mvp.detail.DetailActivity
import com.sstudio.madesubmissionmoviecatalogue.mvp.detail.DetailActivity.Companion.EXTRA_DETAIL
import com.sstudio.madesubmissionmoviecatalogue.mvp.detail.DetailActivity.Companion.movieTv

class FavoriteWidget : AppWidgetProvider() {

    override fun onUpdate(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetIds: IntArray
    ) {
        for (appWidgetId in appWidgetIds) {
            updateAppWidget(
                context,
                appWidgetManager,
                appWidgetId
            )
        }
    }

    override fun onEnabled(context: Context) {

    }

    override fun onDisabled(context: Context) {

    }

    override fun onReceive(context: Context?, intent: Intent?) {
        super.onReceive(context, intent)
        fun Intent.putParcelableExtra(key: String, value: Parcelable) {
            putExtra(key, value)
        }

        if (intent?.action != null) {
            if (intent.action.equals(TOAST_ACTION)) {
//                val viewIndex = intent.getIntExtra(EXTRA_ITEM, 0)
//                Toast.makeText(context, "Touched view $viewIndex", Toast.LENGTH_SHORT).show()
                val movieTv = intent.getIntExtra(EXTRA_ITEM, 0)
                Log.d("mytag", "get $movieTv")
//                val intentDetailActivity = Intent(context, DetailActivity::class.java)
//                intentDetailActivity.putParcelableExtra(EXTRA_DETAIL, movieTv)
//                context?.startActivity(intent)
            }
        }

    }

    companion object {
        private val TOAST_ACTION = "com.sstudio.madesubmissionmoviecatalogue.TOAST_ACTION"
        val EXTRA_ITEM = "com.sstudio.madesubmissionmoviecatalogue.EXTRA_ITEM"
        internal fun updateAppWidget(
            context: Context, appWidgetManager: AppWidgetManager,
            appWidgetId: Int
        ) {

            val intent = Intent(context, StackWidgetService::class.java)
            intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId)
            intent.data = Uri.parse(intent.toUri(Intent.URI_INTENT_SCHEME))

            val views = RemoteViews(context.packageName,
                R.layout.favorite_widget
            )
            views.setRemoteAdapter(R.id.stack_view, intent)
            views.setEmptyView(
                R.id.stack_view,
                R.id.empty_view
            )

            val toastIntent = Intent(context, FavoriteWidget::class.java)
            toastIntent.action =
                TOAST_ACTION
            toastIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId)
            intent.data = Uri.parse(intent.toUri(Intent.URI_INTENT_SCHEME))
            val toastPendingIntent = PendingIntent.getBroadcast(
                context,
                0,
                toastIntent,
                PendingIntent.FLAG_UPDATE_CURRENT
            )
            views.setPendingIntentTemplate(R.id.stack_view, toastPendingIntent)

            appWidgetManager.updateAppWidget(appWidgetId, views)

        }
    }
}


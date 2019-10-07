package com.sstudio.madesubmissionmoviecatalogue.widget

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Parcelable
import android.widget.RemoteViews
import com.sstudio.madesubmissionmoviecatalogue.R
import com.sstudio.madesubmissionmoviecatalogue.model.MovieTv
import com.sstudio.madesubmissionmoviecatalogue.mvp.detail.DetailActivity
import com.sstudio.madesubmissionmoviecatalogue.mvp.detail.DetailActivity.Companion.EXTRA_DETAIL

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
                val movieTv =
                    MovieTv(
                    intent.getStringExtra(EXTRA_POSTER),
                    intent.getStringExtra(EXTRA_OVERVIEW) as String,
                    intent.getStringExtra(EXTRA_RELEASE) as String,
                    intent.getIntExtra(EXTRA_ID, 0),
                    intent.getStringExtra(EXTRA_TITLE) as String,
                    intent.getDoubleExtra(EXTRA_VOTE_AVR, 0.0),
                    intent.getIntExtra(EXTRA_VOTE_CNT, 0),
                    intent.getStringExtra(EXTRA_NAME) as String,
                    intent.getStringExtra(EXTRA_FIRST_AIR) as String,
                    intent.getStringExtra(EXTRA_GENRE) as String,
                    intent.getIntExtra(EXTRA_ISMOVIE, 0)
                )
                val intentDetailActivity = Intent(context, DetailActivity::class.java)
                intentDetailActivity.putParcelableExtra(EXTRA_DETAIL, movieTv)
                intentDetailActivity.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                context?.startActivity(intentDetailActivity)
            }
        }
    }

    companion object {
        private const val TOAST_ACTION = "com.sstudio.madesubmissionmoviecatalogue.TOAST_ACTION"
        const val EXTRA_NAME = "exName"
        const val EXTRA_POSTER = "exPoster"
        const val EXTRA_OVERVIEW = "exOverview"
        const val EXTRA_RELEASE = "exRelease"
        const val EXTRA_ID = "exId"
        const val EXTRA_TITLE = "exTitle"
        const val EXTRA_VOTE_AVR = "exVoteAvr"
        const val EXTRA_VOTE_CNT = "exVoteCnt"
        const val EXTRA_FIRST_AIR = "exFirsAir"
        const val EXTRA_GENRE = "exGenre"
        const val EXTRA_ISMOVIE = "exIsmovie"
        internal fun updateAppWidget(
            context: Context, appWidgetManager: AppWidgetManager,
            appWidgetId: Int
        ) {

            val intent = Intent(context, StackWidgetService::class.java)
            intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId)
            intent.data = Uri.parse(intent.toUri(Intent.URI_INTENT_SCHEME))

            val views = RemoteViews(
                context.packageName,
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


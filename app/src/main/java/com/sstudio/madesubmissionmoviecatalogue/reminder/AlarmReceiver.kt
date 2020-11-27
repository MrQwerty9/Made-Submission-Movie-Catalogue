package com.sstudio.madesubmissionmoviecatalogue.reminder

import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import com.sstudio.madesubmissionmoviecatalogue.App
import com.sstudio.madesubmissionmoviecatalogue.R
import com.sstudio.madesubmissionmoviecatalogue.model.MovieTv
import com.sstudio.madesubmissionmoviecatalogue.model.MoviesResponse
import com.sstudio.madesubmissionmoviecatalogue.mvp.MainActivity
import com.sstudio.madesubmissionmoviecatalogue.mvp.detail.DetailActivity
import com.sstudio.madesubmissionmoviecatalogue.mvp.detail.DetailActivity.Companion.EXTRA_DETAIL
import com.sstudio.madesubmissionmoviecatalogue.mvp.movie.presenter.MovieTvInteractor
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

class AlarmReceiver : BroadcastReceiver() {
    companion object {
        const val TYPE_DAILY = "DailyAlarm"
        const val TYPE_RELEASE = "ReleaseAlarm"
        const val EXTRA_TYPE = "type"
        const val EXTRA_TITLE = "title"
        const val EXTRA_MESSAGE = "message"
    }

    @Inject
    lateinit var movieTvInteractor: MovieTvInteractor
    private val TIME_FORMAT = "HH:mm"
    private val FORMAT_DATE = "yyyy-MM-dd"
    val ID_DAILY = 6301
    val ID_RELEASE = 6302

    override fun onReceive(context: Context, intent: Intent) {
        (context.applicationContext as App).createInteractorComponent().inject(this)
        val type = intent.getStringExtra(EXTRA_TYPE)
        val message = intent.getStringExtra(EXTRA_MESSAGE) as String
        val title = intent.getStringExtra(EXTRA_TITLE) as String

        Log.d("mytag", "onreceive $type")

        if (type == TYPE_DAILY) {
            showAlarmNotification(context, title, message, ID_DAILY, null)
        } else {
            getReleaseToday(context, title, message)
        }

    }

    private fun getReleaseToday(context: Context, title: String?, message: String?) {
        val today = SimpleDateFormat(FORMAT_DATE).format(Date())
        val call = movieTvInteractor.getMovieRelease(
            today,
            today
        )
        call.enqueue(object : Callback<MoviesResponse> {
            override fun onResponse(
                call: Call<MoviesResponse>,
                response: Response<MoviesResponse>
            ) {
                val movies = response.body()?.movieTv
                var id = 0
                movies?.let {
                    for (movie in movies) {
                        id += ID_RELEASE + 1
                        showAlarmNotification(context, title!!, "${movie.title} $message", id, movie)
                    }
                }
            }

            override fun onFailure(call: Call<MoviesResponse>, t: Throwable?) {
                Log.d("mytag", "fail ${t?.message}")
            }
        })
    }

    fun setReminder(context: Context, time: String, type: String) {

        if (isDateInvalid(time)) return

        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(context, AlarmReceiver::class.java)
        val idAlarm: Int

        idAlarm =
            if (type == TYPE_DAILY) {
            intent.putExtra(
                EXTRA_TYPE,
                TYPE_DAILY
            )
            intent.putExtra(
                EXTRA_TITLE, context.getString(
                    R.string.msg_daily_reminder
                ))
            intent.putExtra(
                EXTRA_MESSAGE, context.getString(
                    R.string.msg_daily_reminder_content
                ))
            ID_DAILY
        } else {
            intent.putExtra(
                EXTRA_TYPE,
                TYPE_RELEASE
            )
            intent.putExtra(
                EXTRA_TITLE, context.getString(
                    R.string.msg_released_reminder
                ))
            intent.putExtra(
                EXTRA_MESSAGE,
                context.getString(R.string.msg_released_reminder_content)
            )
            ID_RELEASE
        }

        val timeArray = time.split(":".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()

        val calendar = Calendar.getInstance()
        calendar.set(Calendar.HOUR_OF_DAY, Integer.parseInt(timeArray[0]))
        calendar.set(Calendar.MINUTE, Integer.parseInt(timeArray[1]))
        calendar.set(Calendar.SECOND, 0)

        val pendingIntent = PendingIntent.getBroadcast(context, idAlarm, intent, 0)
        alarmManager.setInexactRepeating(
            AlarmManager.RTC_WAKEUP,
            calendar.timeInMillis,
            AlarmManager.INTERVAL_DAY,
            pendingIntent
        )
    }

    private fun isDateInvalid(date: String): Boolean {
        return try {
            val df = SimpleDateFormat(TIME_FORMAT, Locale.getDefault())
            df.isLenient = false
            df.parse(date)
            false
        } catch (e: ParseException) {
            true
        }

    }

    private fun showAlarmNotification(
        context: Context,
        title: String,
        message: String,
        notifId: Int,
        movieTv: MovieTv?
    ) {
        val CHANNEL_ID = "Channel_1"
        val CHANNEL_NAME = "AlarmManager channel"
        val pendingIntent: PendingIntent
        val notificationManagerCompat =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        if (notifId == ID_DAILY) {
            val intent = Intent(context, MainActivity::class.java)
            pendingIntent = PendingIntent.getActivity(context, 0, intent, 0)
        }else{
            val intent = Intent(context, DetailActivity::class.java)
            intent.putExtra(EXTRA_DETAIL, movieTv!!)
            pendingIntent = PendingIntent.getActivity(context, notifId, intent, PendingIntent.FLAG_UPDATE_CURRENT)
        }
        val builder = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_notifications_active_24dp)
            .setContentIntent(pendingIntent)
            .setContentTitle(title)
            .setContentText(message)
            .setColor(ContextCompat.getColor(context, android.R.color.transparent))
            .setVibrate(longArrayOf(1000, 1000, 1000, 1000, 1000))
            .setSound(alarmSound)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            val channel = NotificationChannel(
                CHANNEL_ID,
                CHANNEL_NAME,
                NotificationManager.IMPORTANCE_DEFAULT
            )

            channel.enableVibration(true)
            channel.vibrationPattern = longArrayOf(1000, 1000, 1000, 1000, 1000)

            builder.setChannelId(CHANNEL_ID)

            notificationManagerCompat.createNotificationChannel(channel)
        }

        val notification = builder.build()

        notificationManagerCompat.notify(notifId, notification)

    }

    fun cancelAlarm(context: Context, id: Int) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager?
        val intent = Intent(context, AlarmReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(context, id, intent, 0)
        pendingIntent.cancel()
        alarmManager?.cancel(pendingIntent)
    }
}
package com.sstudio.madesubmissionmoviecatalogue

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.widget.Toast
import android.app.AlarmManager
import android.app.PendingIntent
import android.util.Log

import java.util.*
import android.app.NotificationManager
import android.app.NotificationChannel
import android.os.Build
import androidx.core.content.ContextCompat
import android.media.RingtoneManager
import androidx.core.app.NotificationCompat
import com.sstudio.madesubmissionmoviecatalogue.model.MoviesResponse
import com.sstudio.madesubmissionmoviecatalogue.mvp.movie.presenter.MovieTvInteractor
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.ParseException
import java.text.SimpleDateFormat
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
    private val ID_REPEATING = 101

    override fun onReceive(context: Context, intent: Intent) {
        (context.applicationContext as App).createInteractorComponent().inject(this)
        val type = intent.getStringExtra(EXTRA_TYPE)
        val message = intent.getStringExtra(EXTRA_MESSAGE).toString()

        val title = intent.getStringExtra(EXTRA_TITLE).toString()
        val notifId = intent.getStringExtra(EXTRA_MESSAGE)

//        showToast(context, title, message)
        if (type == TYPE_DAILY) {
            showAlarmNotification(context, title, message, ID_REPEATING)
        } else {
            getReleaseToday(context, title, message)
        }

    }

    private fun getReleaseToday(context: Context, title: String?, message: String?) {
        val today = SimpleDateFormat(FORMAT_DATE).format(Date())
        val call = movieTvInteractor.getMovieRelease(
            BuildConfig.TMDB_API_KEY,
            context.getString(R.string.language),
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
                        id += ID_REPEATING + 1

                        showAlarmNotification(context, title!!, movie.title + message!!, id)
                    }
                }
            }

            override fun onFailure(call: Call<MoviesResponse>, t: Throwable?) {

            }
        })
    }

    fun setReminder(context: Context, time: String, type: String) {

        if (isDateInvalid(time, TIME_FORMAT)) return

        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(context, AlarmReceiver::class.java)

        if (type == TYPE_DAILY) {
            intent.putExtra(EXTRA_TYPE, TYPE_DAILY)
            intent.putExtra(EXTRA_TITLE, context.getString(R.string.msg_daily_reminder))
            intent.putExtra(EXTRA_MESSAGE, context.getString(R.string.msg_daily_reminder_content))
        } else {
            intent.putExtra(EXTRA_TYPE, TYPE_RELEASE)
            intent.putExtra(EXTRA_TITLE, context.getString(R.string.msg_released_reminder))
            intent.putExtra(
                EXTRA_MESSAGE,
                context.getString(R.string.msg_released_reminder_content)
            )
        }

        val timeArray = time.split(":".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()

        val calendar = Calendar.getInstance()
        calendar.set(Calendar.HOUR_OF_DAY, Integer.parseInt(timeArray[0]))
        calendar.set(Calendar.MINUTE, Integer.parseInt(timeArray[1]))
        calendar.set(Calendar.SECOND, 0)

        val pendingIntent = PendingIntent.getBroadcast(context, ID_REPEATING, intent, 0)
        alarmManager.setInexactRepeating(
            AlarmManager.RTC_WAKEUP,
            calendar.timeInMillis,
            AlarmManager.INTERVAL_DAY,
            pendingIntent
        )
    }

    fun isDateInvalid(date: String, format: String): Boolean {
        try {
            val df = SimpleDateFormat(format, Locale.getDefault())
            df.isLenient = false
            df.parse(date)
            return false
        } catch (e: ParseException) {
            return true
        }

    }

    private fun showAlarmNotification(
        context: Context,
        title: String,
        message: String,
        notifId: Int
    ) {
        val CHANNEL_ID = "Channel_1"
        val CHANNEL_NAME = "AlarmManager channel"

        val notificationManagerCompat =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        val builder = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_favorite_pink_24dp)
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

            notificationManagerCompat?.createNotificationChannel(channel)
        }

        val notification = builder.build()

        notificationManagerCompat?.notify(notifId, notification)

    }

    fun cancelAlarm(context: Context) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(context, AlarmReceiver::class.java)
        val requestCode = ID_REPEATING
        val pendingIntent = PendingIntent.getBroadcast(context, requestCode, intent, 0)
        pendingIntent.cancel()

        alarmManager?.cancel(pendingIntent)

        Toast.makeText(context, "Repeating alarm dibatalkan", Toast.LENGTH_SHORT).show()
    }
}
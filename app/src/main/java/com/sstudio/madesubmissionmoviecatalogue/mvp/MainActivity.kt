package com.sstudio.madesubmissionmoviecatalogue.mvp

import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.sstudio.madesubmissionmoviecatalogue.mvp.movie.view.FavoriteFragment
import com.sstudio.madesubmissionmoviecatalogue.mvp.movie.view.MovieFragment
import com.sstudio.madesubmissionmoviecatalogue.mvp.movie.view.TvShowFragment
import kotlinx.android.synthetic.main.activity_main.*
import android.graphics.BitmapFactory
import android.content.Context.NOTIFICATION_SERVICE
import android.app.NotificationManager
import android.content.Context
import androidx.core.app.NotificationCompat
import android.app.NotificationChannel
import android.os.Build
import android.app.PendingIntent
import android.view.View
import android.net.Uri
import com.sstudio.madesubmissionmoviecatalogue.*


class MainActivity : AppCompatActivity(){

    companion object{
        var isResetViewModel = false
        val NOTIFICATION_ID = 1
        var CHANNEL_ID = "channel_01"
        var CHANNEL_NAME: CharSequence = "dicoding channel"
        var mBuilder: NotificationCompat.Builder? = null
        var mNotificationManager: NotificationManager? = null
    }



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(com.sstudio.madesubmissionmoviecatalogue.R.layout.activity_main)
        setSupportActionBar(toolbar)
        toolbar.title = this.getString(com.sstudio.madesubmissionmoviecatalogue.R.string.app_name)
        bottom_nav.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)
        if (savedInstanceState == null){
            bottom_nav.selectedItemId = com.sstudio.madesubmissionmoviecatalogue.R.id.navigation_movie
        }


    }

    private val runnable =
        Runnable { mNotificationManager?.notify(NOTIFICATION_ID, mBuilder!!.build()) }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(com.sstudio.madesubmissionmoviecatalogue.R.menu.main_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == com.sstudio.madesubmissionmoviecatalogue.R.id.action_change_settings) {
            val mIntent = Intent(Settings.ACTION_LOCALE_SETTINGS)
            startActivity(mIntent)
            isResetViewModel = true
        }
        else if (item.itemId == R.id.reminder){
            startActivity(Intent(this, ReminderActivity::class.java))
        }
        return super.onOptionsItemSelected(item)
    }

    private fun sendNotification(view: View){

        val intent = Intent(Intent.ACTION_VIEW, Uri.parse("http://dicoding.com"))
        val pendingIntent = PendingIntent.getActivity(this, 0, intent, 0)
        val mNotificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        val mBuilder = NotificationCompat.Builder(this, CHANNEL_ID)
            .setSmallIcon(com.sstudio.madesubmissionmoviecatalogue.R.drawable.ic_favorite_white_24dp)
            .setLargeIcon(
                BitmapFactory.decodeResource(
                    resources,
                    com.sstudio.madesubmissionmoviecatalogue.R.drawable.ic_favorite_pink_24dp
                )
            )
            .setContentTitle(resources.getString(com.sstudio.madesubmissionmoviecatalogue.R.string.content_title))
            .setContentText(resources.getString(com.sstudio.madesubmissionmoviecatalogue.R.string.content_text))
            .setSubText(resources.getString(com.sstudio.madesubmissionmoviecatalogue.R.string.subtext))
            .setAutoCancel(true)

        val notification = mBuilder.build()

        mNotificationManager?.notify(NOTIFICATION_ID, notification)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            /* Create or update. */
            val channel = NotificationChannel(
                CHANNEL_ID,
                CHANNEL_NAME,
                NotificationManager.IMPORTANCE_DEFAULT
            )

            mBuilder.setChannelId(CHANNEL_ID)

            mNotificationManager?.createNotificationChannel(channel)
        }
    }

    private val mOnNavigationItemSelectedListener =
        object : BottomNavigationView.OnNavigationItemSelectedListener {
            override fun onNavigationItemSelected(item: MenuItem): Boolean {
                val fragment: Fragment

                when (item.itemId) {
                    com.sstudio.madesubmissionmoviecatalogue.R.id.navigation_movie -> {

                        fragment =
                            MovieFragment()
                        supportFragmentManager.beginTransaction()
                            .replace(
                                com.sstudio.madesubmissionmoviecatalogue.R.id.layout_container,
                                fragment,
                                fragment::class.java.simpleName
                            )
                            .commit()
                        return true
                    }
                    com.sstudio.madesubmissionmoviecatalogue.R.id.navigation_tv -> {

                        fragment =
                            TvShowFragment()
                        supportFragmentManager.beginTransaction()
                            .replace(
                                com.sstudio.madesubmissionmoviecatalogue.R.id.layout_container,
                                fragment,
                                fragment::class.java.simpleName
                            )
                            .commit()
                        return true
                    }
                    com.sstudio.madesubmissionmoviecatalogue.R.id.navigation_favorite -> {

                        fragment =
                            FavoriteFragment()
                        supportFragmentManager.beginTransaction()
                            .replace(
                                com.sstudio.madesubmissionmoviecatalogue.R.id.layout_container,
                                fragment,
                                fragment::class.java.simpleName
                            )
                            .commit()
                        return true
                    }
                }
                return false
            }
        }
}

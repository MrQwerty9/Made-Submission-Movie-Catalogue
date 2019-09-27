package com.sstudio.madesubmissionmoviecatalogue

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_reminder.*

class ReminderActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reminder)
        setSupportActionBar(toolbar)
        toolbar.title = this.getString(com.sstudio.madesubmissionmoviecatalogue.R.string.app_name)
        supportFragmentManager.beginTransaction().add(R.id.reminder_preference, SettingPreference())
            .commit()
    }
}

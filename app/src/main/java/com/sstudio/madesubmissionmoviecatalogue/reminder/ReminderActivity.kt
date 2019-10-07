package com.sstudio.madesubmissionmoviecatalogue.reminder

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.sstudio.madesubmissionmoviecatalogue.R
import com.sstudio.madesubmissionmoviecatalogue.preference.SettingPreference
import kotlinx.android.synthetic.main.activity_reminder.*

class ReminderActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reminder)
        setSupportActionBar(toolbar)
        toolbar.title = this.getString(R.string.app_name)
        supportFragmentManager.beginTransaction().add(
            R.id.reminder_preference,
            SettingPreference()
        )
            .commit()
    }
}

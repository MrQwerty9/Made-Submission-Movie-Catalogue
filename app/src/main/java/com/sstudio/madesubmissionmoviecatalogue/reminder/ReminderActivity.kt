package com.sstudio.madesubmissionmoviecatalogue.reminder

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.sstudio.madesubmissionmoviecatalogue.R
import com.sstudio.madesubmissionmoviecatalogue.preference.GDriveUrlSettingPreference
import com.sstudio.madesubmissionmoviecatalogue.preference.ReminderSettingPreference
import kotlinx.android.synthetic.main.activity_reminder.*

class ReminderActivity : AppCompatActivity() {

    companion object{
        val SETTING_EXTRA = "setting_extra"
        val SETTING_REMINDER = "setting_reminder"
        val SETTING_GDRIVE = "setting_google_drive"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reminder)
        setSupportActionBar(toolbar)
        toolbar.title = this.getString(R.string.app_name)
        val intent = intent.getStringExtra(SETTING_EXTRA)
        Log.d("mytag", "intent $intent")
        if (intent == SETTING_REMINDER) {
            supportFragmentManager.beginTransaction().add(
                R.id.reminder_preference,
                ReminderSettingPreference()
            )
                .commit()
        }else if (intent == SETTING_GDRIVE){
            supportFragmentManager.beginTransaction().add(
                R.id.reminder_preference,
                GDriveUrlSettingPreference(this)
            )
                .commit()
        }
    }
}

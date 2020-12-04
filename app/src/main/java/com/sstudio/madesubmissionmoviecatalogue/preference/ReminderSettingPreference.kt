package com.sstudio.madesubmissionmoviecatalogue.preference

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Toast
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.SwitchPreference
import com.sstudio.madesubmissionmoviecatalogue.R
import com.sstudio.madesubmissionmoviecatalogue.reminder.AlarmReceiver

class ReminderSettingPreference: PreferenceFragmentCompat(),
    SharedPreferences.OnSharedPreferenceChangeListener {

    private val DAILY_KEY: String = "sp_daily"
    private val RELEASE_KEY: String = "sp_release"

    private var dailyPreference: SwitchPreference? = null
    private var releasePreference: SwitchPreference? = null
    private val alarmReceiver = AlarmReceiver()

    override fun onCreatePreferences(bundle: Bundle?, s: String?) {
        addPreferencesFromResource(R.xml.reminder_preferences)
        init()
        setSummaries()
    }

    override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences?, key: String?) {
        sharedPreferences?.let {
            context?.let {context ->
                if (key.equals(DAILY_KEY)) {
                    dailyPreference?.isChecked = it.getBoolean(DAILY_KEY, false)
                    if (it.getBoolean(DAILY_KEY, false)) {
                        alarmReceiver.setReminder(
                            context,
                            "07:00",
                            AlarmReceiver.TYPE_DAILY
                        )
                        showToast(context, context.getString(R.string.msg_daily_set))
                    } else{
                        alarmReceiver.cancelAlarm(context, alarmReceiver.ID_DAILY)
                        showToast(context, context.getString(R.string.msg_daily_unset))
                    }
                }
                if (key.equals(RELEASE_KEY)) {
                    releasePreference?.isChecked = it.getBoolean(RELEASE_KEY, false)
                    if (it.getBoolean(RELEASE_KEY, false)) {
                        alarmReceiver.setReminder(
                            context,
                            "08:00",
                            AlarmReceiver.TYPE_RELEASE
                        )
                        showToast(context, context.getString(R.string.msg_released_set))
                    } else{
                        alarmReceiver.cancelAlarm(context, alarmReceiver.ID_RELEASE)
                        showToast(context, context.getString(R.string.msg_released_unset))
                    }
                }
            }
        }
    }

    private fun init() {
        dailyPreference = findPreference(DAILY_KEY) as SwitchPreference?
        releasePreference = findPreference(RELEASE_KEY) as SwitchPreference?
    }

    private fun setSummaries() {
        val sh = preferenceManager.sharedPreferences
        dailyPreference?.isChecked = sh.getBoolean(DAILY_KEY, false)
        releasePreference?.isChecked = sh.getBoolean(RELEASE_KEY, false)
    }

    override fun onResume() {
        super.onResume()
        preferenceScreen.sharedPreferences.registerOnSharedPreferenceChangeListener(this)
    }

    override fun onPause() {
        super.onPause()
        preferenceScreen.sharedPreferences.unregisterOnSharedPreferenceChangeListener(this)
    }

    private fun showToast(context: Context, message: String) {
        Toast.makeText(context, message, Toast.LENGTH_LONG).show()
    }
}
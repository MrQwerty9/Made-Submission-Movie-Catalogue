package com.sstudio.madesubmissionmoviecatalogue.mvp

import android.content.BroadcastReceiver
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.viewpager.widget.ViewPager
import kotlinx.android.synthetic.main.activity_main.*
import android.content.Intent
import android.content.IntentFilter
import android.net.ConnectivityManager
import android.provider.Settings
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import com.google.android.material.tabs.TabLayout
import com.sstudio.madesubmissionmoviecatalogue.MyReceiver
import com.sstudio.madesubmissionmoviecatalogue.R
import com.sstudio.madesubmissionmoviecatalogue.adapter.FragmentAdapter



class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setupToolbar()
        init()
    }

    private fun init() {
        setupViewPager(pager_container)
        tab_layout.setupWithViewPager(pager_container)
Log.d("main", "abcde")
    }

    private fun setupViewPager(viewPager: ViewPager) {
        val adapter = FragmentAdapter(supportFragmentManager, this)
        viewPager.offscreenPageLimit = FragmentAdapter.PAGE_COUNT
        viewPager.addOnPageChangeListener(TabLayout.TabLayoutOnPageChangeListener(tab_layout))
        viewPager.adapter = adapter
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.action_change_settings) {
            val mIntent = Intent(Settings.ACTION_LOCALE_SETTINGS)
            startActivity(mIntent)
        }
        return super.onOptionsItemSelected(item)
    }


    private fun setupToolbar(){
        setSupportActionBar(toolbar)
        supportActionBar?.title = "Movie Catalogue";

    }
}

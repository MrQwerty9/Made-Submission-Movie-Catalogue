package com.sstudio.madesubmissionmoviecatalogue.mvp

import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.sstudio.madesubmissionmoviecatalogue.R
import com.sstudio.madesubmissionmoviecatalogue.mvp.movie.view.FavoriteFragment
import com.sstudio.madesubmissionmoviecatalogue.mvp.movie.view.MovieHomeFragment
import com.sstudio.madesubmissionmoviecatalogue.mvp.movie.view.TvShowFragment
import com.sstudio.madesubmissionmoviecatalogue.reminder.ReminderActivity
import kotlinx.android.synthetic.main.activity_main.*
import javax.net.ssl.SSLContext
import javax.net.ssl.SSLEngine


class MainActivity : AppCompatActivity(){

    companion object{
        var isResetViewModel = false
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)
        toolbar.title = this.getString(R.string.app_name)
        bottom_nav.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)
        if (savedInstanceState == null){
            bottom_nav.selectedItemId = R.id.navigation_movie
        }


    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.action_change_settings) {
            val mIntent = Intent(Settings.ACTION_LOCALE_SETTINGS)
            startActivity(mIntent)
            isResetViewModel = true
        }
        else if (item.itemId == R.id.reminder){
            startActivity(Intent(this, ReminderActivity::class.java))
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onBackPressed() {
//        val fm: FragmentManager = fragmentManager
//        if (fm.backStackEntryCount > 0) {
////            Log.i("MainActivity", "popping backstack")
//            fm.popBackStack()
//        } else {
////            Log.i("MainActivity", "nothing on backstack, calling super")
//            super.onBackPressed()
//        }
        super.onBackPressed()
        fragmentManager.popBackStackImmediate()
    }

    private val mOnNavigationItemSelectedListener =
        object : BottomNavigationView.OnNavigationItemSelectedListener {
            override fun onNavigationItemSelected(item: MenuItem): Boolean {
                val fragment: Fragment

                when (item.itemId) {
                    R.id.navigation_movie -> {

                        fragment =
                            MovieHomeFragment()
                        supportFragmentManager.beginTransaction()
                            .replace(
                                R.id.layout_container,
                                fragment,
                                fragment::class.java.simpleName
                            )
                            .commit()
                        return true
                    }
                    R.id.navigation_tv -> {

                        fragment =
                            TvShowFragment()
                        supportFragmentManager.beginTransaction()
                            .replace(
                                R.id.layout_container,
                                fragment,
                                fragment::class.java.simpleName
                            )
                            .commit()
                        return true
                    }
                    R.id.navigation_favorite -> {

                        fragment =
                            FavoriteFragment()
                        supportFragmentManager.beginTransaction()
                            .replace(
                                R.id.layout_container,
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

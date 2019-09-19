package com.sstudio.madesubmissionmoviecatalogue.adapter

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.sstudio.madesubmissionmoviecatalogue.R
import com.sstudio.madesubmissionmoviecatalogue.mvp.movie.view.MovieFragment
import com.sstudio.madesubmissionmoviecatalogue.mvp.movie.view.TvShowFragment

class TabAdapter(fm: FragmentManager, private val context: Context, private val page_count: Int): FragmentPagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

    override fun getItem(position: Int): Fragment {
        var fragment: Fragment? = null

        when (position) {
            0 -> fragment = MovieFragment()
            1 -> fragment = TvShowFragment()
        }

        return fragment as Fragment
    }

    override fun getPageTitle(position: Int): CharSequence? {
        when (position) {
            0 -> return context.resources.getString(R.string.tab_movie)
            1 -> return context.resources.getString(R.string.tab_tv)
        }

        return null
    }

    override fun getCount(): Int {
        return page_count
    }
}
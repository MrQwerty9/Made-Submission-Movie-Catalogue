package com.sstudio.madesubmissionmoviecatalogue.mvp.movie.view


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.android.material.tabs.TabLayout
import com.sstudio.madesubmissionmoviecatalogue.R
import com.sstudio.madesubmissionmoviecatalogue.adapter.TabAdapter
import kotlinx.android.synthetic.main.fragment_favorite.view.*

class FavoriteFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_favorite, container, false)
        setupViewPager(view)
        return view

    }

    private fun setupViewPager(view: View) {
        val adapter = context?.let { TabAdapter(childFragmentManager, it, 2) }
        view.pager_container.offscreenPageLimit = 2
        view.pager_container.addOnPageChangeListener(TabLayout.TabLayoutOnPageChangeListener(view.tab_layout))
        view.pager_container.adapter = adapter
        view.tab_layout.setupWithViewPager(view.pager_container)
    }
}

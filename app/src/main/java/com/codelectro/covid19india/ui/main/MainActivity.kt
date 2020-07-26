package com.codelectro.covid19india.ui.main

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.text.HtmlCompat
import androidx.navigation.Navigation
import androidx.navigation.ui.NavigationUI
import com.codelectro.covid19india.R
import com.codelectro.covid19india.ui.MainViewModel
import com.codelectro.covid19india.ui.color
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_main.*


@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    companion object {
        const val TAG = "MainActivity "
    }

    val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val title = "Covid19".color("#1530A5") + ".India".color("#FFC107")

        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        toolbar_title.text = HtmlCompat.fromHtml(title, HtmlCompat.FROM_HTML_MODE_LEGACY);
        setUpNavigation()

    }

    private fun setUpNavigation() {
        val navController = Navigation.findNavController(this, R.id.nav_host_fragment)
        NavigationUI.setupWithNavController(bottom_nav, navController)
    }


}

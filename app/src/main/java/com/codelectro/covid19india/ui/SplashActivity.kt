package com.codelectro.covid19india.ui

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import com.codelectro.covid19india.ui.main.MainActivity


class SplashActivity : AppCompatActivity() {
    private val SPLASH_TIME_OUT:Long = 200 // 1 sec
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Handler().postDelayed({
            startActivity(Intent(this,MainActivity::class.java))
            finish()
        }, SPLASH_TIME_OUT)
    }
}
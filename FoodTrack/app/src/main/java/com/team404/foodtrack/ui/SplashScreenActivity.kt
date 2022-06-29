package com.team404.foodtrack.ui

import android.annotation.SuppressLint
import android.app.ActivityOptions
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import com.team404.foodtrack.MainActivity
import com.team404.foodtrack.R

@SuppressLint("CustomSplashScreen")
class SplashScreenActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        supportActionBar!!.hide()

        startTimer()
    }

    private fun startTimer() {
        object: CountDownTimer(3500, 1000) {
            override fun onTick(p0: Long) {
            }

            override fun onFinish() {
                val intent = Intent(applicationContext, MainActivity::class.java)
                startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(this@SplashScreenActivity).toBundle())

            }
        }.start()
    }
}
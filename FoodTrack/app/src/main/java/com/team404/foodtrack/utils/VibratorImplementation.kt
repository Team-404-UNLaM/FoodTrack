package com.team404.foodtrack.utils

import android.content.Context
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager

object VibratorImplementation {
    private lateinit var vibrator : Vibrator
    //Vibrate Pattern
    private const val DELAY = 0L
    private const val VIBRATE = 300L
    private const val  SLEEP = 300L
    private val VIBRATE_PATTERN  = longArrayOf(DELAY, VIBRATE, SLEEP)
    //To disable repeating vibrate pattern
    private const val START = -1

    private fun initVibrator(context: Context) {
        vibrator = if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.S){
            val vibratorManager = context.getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as VibratorManager
            vibratorManager.defaultVibrator
        }else{
            //For android versions lower than S (API 31) => Android 12
            context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
        }
    }

    fun vibrate(context: Context) {
        initVibrator(context)
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            vibrator.vibrate(
                VibrationEffect.createWaveform(
                    VIBRATE_PATTERN,
                    START
                ))
        }else{
            //For android versions lower than O (API 26) => Android 8 (OREO)
            vibrator.vibrate(VIBRATE_PATTERN, START)
        }
    }
}
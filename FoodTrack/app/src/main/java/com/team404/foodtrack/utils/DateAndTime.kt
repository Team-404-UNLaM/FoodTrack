package com.team404.foodtrack.utils

import android.os.Build
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*

object DateAndTime {
    fun getDateAndTime():String{
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            val current = LocalDateTime.now()
            val formatter = DateTimeFormatter.ofPattern("HH:mm dd/MM/yyyy")
            return current.format(formatter)
        }else{
            val date = Calendar.getInstance().time
            val format = "HH:mm dd/MM/yyyy"
            val locale = Locale.getDefault()
            val formatter = SimpleDateFormat(format, locale)
            return formatter.format(date)
        }
    }
}
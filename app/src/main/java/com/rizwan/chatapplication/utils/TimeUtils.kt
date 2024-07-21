package com.rizwan.chatapplication.utils

import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

object TimeUtils {

    fun isMoreThanAnHour(previousChatTime: Long, currentChatTime: Long): Boolean {
        val differenceInMillis = currentChatTime - previousChatTime
        val differenceInHours = differenceInMillis / (1000 * 60 * 60)

        return differenceInHours > 0
    }

    fun getFormattedDay(currentChatTime: Long): String {
        val formatter = SimpleDateFormat("EEEE", Locale.getDefault())
        val currCal = Calendar.getInstance()
        currCal.timeInMillis = currentChatTime
        return formatter.format(currCal.time)
    }

    fun getFormattedTime(currentChatTime: Long): String {
        val formatter = SimpleDateFormat("H:mm", Locale.getDefault())
        val currCal = Calendar.getInstance()
        currCal.timeInMillis = currentChatTime
        return formatter.format(currCal.time)
    }

}
package com.psssum.docs.utils

import android.content.Context
import android.os.Build
import com.psssum.market.utils.L
import java.text.SimpleDateFormat
import java.time.Year
import java.util.*

fun formatDate(time : Long, context: Context) : String{
    val minusSecs = 8640000
    //100 days
    val dateCalendar = Calendar.getInstance()
    dateCalendar.timeInMillis = time*1000
    val currentDayCalendar = Calendar.getInstance()

    if (dateCalendar.get(Calendar.DATE) == currentDayCalendar.get(Calendar.DATE)){
        return "Сегодня"
    } else if (currentDayCalendar.get(Calendar.DATE) - dateCalendar.get(Calendar.DATE) == 1) {
        return "Вчера"
    } else if ((System.currentTimeMillis()/1000 - minusSecs) >= time){
        return com.psssum.market.utils.buildString(format(time, context, "dd MMM"), " " ,format(time, context, "YYYY")).toString()
    } else {
        return format(time, context, "dd MMM")
    }

}
fun formatDateAbandoned(time : Long, context: Context) : String{
    val date = Date(time*1000)
    val formatter = SimpleDateFormat("dd MMM yyyy", getCurrentLocale(context))
    formatter.setTimeZone(TimeZone.getTimeZone("UTC"))

    var formatted = formatter.format(date)
    return formatted
}
fun format(time : Long, context: Context, pattern : String) : String{
    val date = Date(time*1000)
    val formatter = SimpleDateFormat(pattern, getCurrentLocale(context))
    formatter.setTimeZone(TimeZone.getTimeZone("UTC"))

    var formatted = formatter.format(date)
    if (formatted.length > 6){
        formatted = formatted.substring(0, 6)
    }
    return formatted
}

fun getCurrentLocale(context: Context) : Locale{
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N){
        context.resources.configuration.locales.get(0);
    } else{
        //noinspection deprecation
        context.resources.configuration.locale;
    }
}
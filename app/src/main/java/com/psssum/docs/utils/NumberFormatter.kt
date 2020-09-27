package com.psssum.docs.utils

import com.psssum.market.utils.buildString
import java.lang.Math.round

fun formatNumber(number : Long) : String{
    var numberString = ""
    val mFloated = Math.abs(number.toFloat() / 1000000)
    val kFloated = Math.abs(number.toFloat() / 1000)
    numberString = if (mFloated > 1) {
        buildString((Math.round(mFloated * 10.0) / 10.0).toString()," МБ").toString()
    } else {
        buildString((Math.round(kFloated * 10.0) / 10.0).toString(), " КБ").toString()
    }
    return numberString
}
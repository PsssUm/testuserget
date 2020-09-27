package com.psssum.market.utils

import java.lang.StringBuilder

fun buildString(vararg strings: String) : StringBuilder{
    val result = StringBuilder()
    for (s in strings){
        result.append(s)
    }
    return result
}
package com.psssum.docs.ui.customViews

import android.content.Context
import android.graphics.Color
import android.graphics.Typeface
import android.view.Gravity
import androidx.core.content.ContextCompat
import androidx.lifecycle.Lifecycle
import com.psssum.docs.R
import com.psssum.docs.ui.adapters.MenuAdapter
import com.psssum.docs.utils.ConvertUtils
import com.skydoves.powermenu.CustomPowerMenu
import com.skydoves.powermenu.MenuAnimation
import com.skydoves.powermenu.MenuBaseAdapter
import com.skydoves.powermenu.PowerMenuItem

fun getMenu(ctx : Context): CustomPowerMenu<*, out MenuBaseAdapter<*>>? {
    val menu = CustomPowerMenu.Builder(ctx, MenuAdapter())
        .addItem(PowerMenuItem("Переименовать", false))
        .addItem(PowerMenuItem("Удалить документ", false))
        //setAutoDismiss(true)

        .setAnimation(MenuAnimation.FADE)
        .setBackgroundAlpha(0.0f)

        .setSize(ConvertUtils.convertDpToPixel(190f, ctx).toInt(), ConvertUtils.convertDpToPixel(180f, ctx).toInt())
        .setInitializeRule(Lifecycle.Event.ON_CREATE, 0)
        .setMenuRadius(ConvertUtils.convertDpToPixel(16f, ctx))
        .setMenuShadow(16f)
        .build()
    return menu
}
package com.psssum.docs.ui.customViews

import android.content.Context
import android.graphics.Color
import android.graphics.Typeface
import android.view.Gravity
import androidx.core.content.ContextCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import com.psssum.docs.R
import com.psssum.docs.utils.ConvertUtils
import com.skydoves.powermenu.*
import com.skydoves.powermenu.kotlin.createPowerMenu

class MoreMenuFactory : PowerMenu.Factory() {

    override fun create(context: Context, lifecycle: LifecycleOwner): PowerMenu {
        return createPowerMenu(context) {
            addItem(PowerMenuItem("Переименовать", false))
            addItem(PowerMenuItem("Удалить документ", false))
            //setAutoDismiss(true)

            setLifecycleOwner(lifecycle)
            setAnimation(MenuAnimation.FADE)
            setTextColor(ContextCompat.getColor(context, R.color.black))
            setTextSize(16)
            setBackgroundAlpha(0f)
            setMenuRadius(ConvertUtils.convertDpToPixel(16f, context))
            setMenuShadow(16f)
            setTextTypeface(Typeface.create("sans-serif", Typeface.NORMAL))
            setTextGravity(Gravity.START)
            setMenuColor(Color.WHITE)
            //setWidth(ConvertUtils.convertDpToPixel(182f, context).toInt()) // sets the popup width size.
            //setHeight(ConvertUtils.convertDpToPixel(150f, context).toInt())

            //setSize(ConvertUtils.convertDpToPixel(182f, context).toInt(), ConvertUtils.convertDpToPixel(120f, context).toInt())
            setInitializeRule(Lifecycle.Event.ON_CREATE, 0)

        }
    }
}

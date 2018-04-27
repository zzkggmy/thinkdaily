package com.byren.kai.thinkdaily.utils

import android.app.Activity
import android.view.WindowManager

object setBackGroundAlpha {
    fun set(activity: Activity, alpha: Float) {
        val lp: WindowManager.LayoutParams = activity.window.attributes
        lp.alpha = alpha
        activity.window.attributes = lp
    }

}
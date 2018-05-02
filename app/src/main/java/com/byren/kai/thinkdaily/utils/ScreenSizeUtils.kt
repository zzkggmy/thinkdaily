package com.byren.kai.thinkdaily.utils

import android.annotation.SuppressLint
import android.util.DisplayMetrics
import android.view.WindowManager

object ScreenSizeUtils {
    @SuppressLint("WrongConstant")
    private val manager: WindowManager = Common.mContext().getSystemService("window") as WindowManager
    private var dm: DisplayMetrics = DisplayMetrics()
    private var screenWidth = 0
    private var screenHeight = 0
    init {
        manager.defaultDisplay.getMetrics(dm)
        screenWidth = dm.widthPixels
        screenHeight = dm.heightPixels
    }
    fun getScreenWidth(): Int {return this.screenWidth
    }
    fun getScreenHeight(): Int {return this.screenHeight
    }
}
package com.byren.kai.thinkdaily.utils

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Application
import android.content.Context

@SuppressLint("StaticFieldLeak")
object Common {
    lateinit var context: Context
    fun with(app: Application) {
        context = app
    }
    fun mContext(): Context{
        return context
    }
}
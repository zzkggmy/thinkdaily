package com.byren.kai.thinkdaily

import android.app.Application
import com.byren.kai.thinkdaily.utils.Common

class App: Application() {
    override fun onCreate() {
        super.onCreate()
        Common.with(this)
    }

}

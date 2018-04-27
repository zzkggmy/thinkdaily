package com.byren.kai.thinkdaily.activity

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.SharedPreferences
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.support.annotation.RequiresApi
import android.support.v7.app.AppCompatActivity
import android.view.*
import com.byren.kai.thinkdaily.R
import kotlinx.android.synthetic.main.activity_base.*

/**
 * Created by Lucas on 2018/3/26 .
 */
open class BaseActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_base)
        val params: ViewGroup.LayoutParams = v_base.layoutParams
        params.height = getStatusBarHeight()
        v_base.layoutParams = params
    }

    override fun setContentView(layoutResID: Int) {
        super.setContentView(layoutResID)
        val contentView: View = LayoutInflater.from(this).inflate(layoutResID, null)
        fl_base.addView(contentView)
    }

    private fun getStatusBarHeight(): Int {
        var statusBarHeight = 0
        val resourceId = resources.getIdentifier("status_bar_height", "dimen", "android")
        if (resourceId > 0) {
            statusBarHeight = resources.getDimensionPixelSize(resourceId)
        }
        return statusBarHeight
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    protected fun setImmersiveStatusBar(view: View, activity: Activity) {
        setTranslucentStatus(activity)
        setDark(activity)
        view.setBackgroundResource(R.color.zhihu_primary)
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT) {
            val window: Window = activity.window
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            view.setPadding(0, getStatusBarHeight(), 0, 0)
        }
    }

    private fun setDark(activity: Activity) {

                activity.window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN

    }

    private fun setTranslucentStatus(activity: Activity) {
        val window: Window = activity.window
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            window.statusBarColor = Color.TRANSPARENT
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS or WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION)
        }
    }

    fun checkFont(color: Int, activity: Activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (color == resources.getColor(R.color.white)) {
                activity.window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
            } else {
                activity.window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
            }
        }
    }
}

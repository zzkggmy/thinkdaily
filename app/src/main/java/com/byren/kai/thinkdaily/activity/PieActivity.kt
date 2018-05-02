package com.byren.kai.thinkdaily.activity

import android.annotation.SuppressLint
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.byren.kai.thinkdaily.R
import com.byren.kai.thinkdaily.utils.StatusBarUtil

@SuppressLint("Registered")
class PieActivity : AppCompatActivity(){
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pie)
        StatusBarUtil.setColor(this)
    }
}
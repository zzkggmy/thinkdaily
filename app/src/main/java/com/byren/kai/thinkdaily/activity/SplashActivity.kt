package com.byren.kai.thinkdaily.activity

import android.animation.ObjectAnimator
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.support.v7.app.AppCompatActivity
import com.byren.kai.thinkdaily.R
import com.byren.kai.thinkdaily.utils.StatusBarUtil
import kotlinx.android.synthetic.main.activity_splash.*

class SplashActivity : AppCompatActivity(){

    private var objectAnimation: ObjectAnimator? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        StatusBarUtil.setColor(this)
        objectAnimation = ObjectAnimator.ofFloat(iv_splash,"scaleX",1f,1.5f)
        objectAnimation!!.duration = 1500
        objectAnimation!!.start()
        Handler().postDelayed({
            startActivity(Intent(this,MainActivity::class.java))
            finish()
        },1500)
    }

    override fun onDestroy() {
        super.onDestroy()

    }
}
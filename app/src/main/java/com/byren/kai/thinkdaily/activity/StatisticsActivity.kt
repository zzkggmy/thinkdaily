package com.byren.kai.thinkdaily.activity

import android.app.Activity
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.support.annotation.RequiresApi
import android.support.v4.app.FragmentActivity
import android.support.v7.app.AppCompatActivity
import android.view.KeyEvent
import android.view.View
import android.view.Window
import android.view.WindowManager
import com.byren.kai.thinkdaily.R
import com.byren.kai.thinkdaily.fragment.ExpenditureFragment
import com.byren.kai.thinkdaily.fragment.IncomeFragment
import com.byren.kai.thinkdaily.utils.SpUtils
import com.byren.kai.thinkdaily.utils.StatusBarUtil
import kotlinx.android.synthetic.main.activity_statistics.*

/**
 * Created by Lucas on 2018/3/26 .
 */
class StatisticsActivity : FragmentActivity(), View.OnClickListener {

    private val incomeFrag: IncomeFragment = IncomeFragment()
    private val expenditureFrag: ExpenditureFragment = ExpenditureFragment()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_statistics)
        initViewStatus()
        iv_back_statistics.setOnClickListener(this)
        tv_income_tb_statistics.setOnClickListener(this)
        tv_expenditure_tb_statistics.setOnClickListener(this)
    }

    private fun initViewStatus() {
        tv_income_tb_statistics.setBackgroundResource(R.drawable.statistics_tb_left_corner)
        tv_income_tb_statistics.setTextColor(resources.getColor(R.color.zhihu_primary))
        tv_expenditure_tb_statistics.setBackgroundResource(R.color.zhihu_primary)
        tv_expenditure_tb_statistics.setTextColor(resources.getColor(R.color.white))
        supportFragmentManager.beginTransaction().add(R.id.fl_statistics, incomeFrag)
                .add(R.id.fl_statistics, expenditureFrag).show(incomeFrag)
                .hide(expenditureFrag).commit()
    }

    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.iv_back_statistics -> finish()
            R.id.tv_income_tb_statistics -> {
                tv_income_tb_statistics.setBackgroundResource(R.drawable.statistics_tb_left_corner)
                tv_income_tb_statistics.setTextColor(resources.getColor(R.color.zhihu_primary))
                tv_expenditure_tb_statistics.setBackgroundResource(R.color.zhihu_primary)
                tv_expenditure_tb_statistics.setTextColor(resources.getColor(R.color.white))
                supportFragmentManager.beginTransaction().show(incomeFrag)
                        .hide(expenditureFrag).commit()
            }
            R.id.tv_expenditure_tb_statistics -> {
                tv_income_tb_statistics.setBackgroundResource(R.color.zhihu_primary)
                tv_income_tb_statistics.setTextColor(resources.getColor(R.color.white))
                tv_expenditure_tb_statistics.setBackgroundResource(R.drawable.statistics_tb_right_corner)
                tv_expenditure_tb_statistics.setTextColor(resources.getColor(R.color.zhihu_primary))
                supportFragmentManager.beginTransaction().show(expenditureFrag)
                        .hide(incomeFrag).commit()
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onResume() {
        super.onResume()
        StatusBarUtil.setColor(this)
    }

    override fun onDestroy() {
        super.onDestroy()
        SpUtils.spSetRvMode(1)
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        if (keyCode == KeyEvent.KEYCODE_BACK && event!!.repeatCount == 0) {
            finish()
        }
        return super.onKeyDown(keyCode, event)
    }
}
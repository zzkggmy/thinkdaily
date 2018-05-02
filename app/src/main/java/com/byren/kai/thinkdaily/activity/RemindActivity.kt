package com.byren.kai.thinkdaily.activity

import android.annotation.SuppressLint
import android.app.Activity
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.provider.Settings
import android.support.annotation.RequiresApi
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.helper.ItemTouchHelper
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.WindowManager
import android.widget.CalendarView
import android.widget.LinearLayout
import android.widget.PopupWindow
import android.widget.TextView
import com.byren.kai.thinkdaily.R
import com.byren.kai.thinkdaily.adapter.RemindAdapter
import com.byren.kai.thinkdaily.beans.Remind
import com.byren.kai.thinkdaily.utils.*
import kotlinx.android.synthetic.main.activity_remind.*
import kotlinx.android.synthetic.main.add_remind_popup.*
import kotlinx.android.synthetic.main.add_remind_popup.view.*
import kotlinx.android.synthetic.main.datepopup.view.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


class RemindActivity : AppCompatActivity(), View.OnClickListener {
    private var handler: Handler? = null
    private var date = ""
    private var list: ArrayList<Remind> = ArrayList()
    private val linearLayoutManager = LinearLayoutManager(this)
    private var adapter: RemindAdapter? = null
    @SuppressLint("Recycle", "SimpleDateFormat")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_remind)
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN)
        initAdapter()
        handler = @SuppressLint("HandlerLeak")
        object : Handler() {
            override fun handleMessage(msg: Message) {
                super.handleMessage(msg)
                when (msg.what) {
                    1 -> {
                        adapter = RemindAdapter(this@RemindActivity, remindDbManager.conditionQuery(date))
                        val helper = AdapterItemTouchHelper(adapter)
                        val itemTouchHelper = ItemTouchHelper(helper)
                        itemTouchHelper.attachToRecyclerView(rv_remind)
                        rv_remind.adapter = adapter
                        rv_remind.layoutManager = linearLayoutManager
                        rv_remind.adapter.notifyDataSetChanged()
                    }
                }
            }
        }
        iv_remind_back.setOnClickListener(this)
        iv_date_remind.setOnClickListener(this)
        tv_add_remind.setOnClickListener(this)
        val simpleFormat = SimpleDateFormat("yyyy-M-dd")
        val date = Date(System.currentTimeMillis())
        tv_show_date_remind.text = simpleFormat.format(date).toString()
    }

    private fun initAdapter() {
        adapter = RemindAdapter(this, remindDbManager.getRemindDb())
        val helper = AdapterItemTouchHelper(adapter)
        val itemTouchHelper = ItemTouchHelper(helper)
        itemTouchHelper.attachToRecyclerView(rv_remind)
        rv_remind.adapter = adapter
        rv_remind.layoutManager = linearLayoutManager
        rv_remind.adapter.notifyDataSetChanged()
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onResume() {
        super.onResume()
        StatusBarUtil.setColor(this)
//        setImmersiveStatusBar(tb_remind, this)
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.iv_remind_back -> finish()
            R.id.tv_add_remind -> showPopUp()
            R.id.iv_date_remind -> {
                tv_show_date_remind.showDate(this, handler!!)
            }
        }
    }

    @SuppressLint("SimpleDateFormat", "SetTextI18n")
    private fun showPopUp() {
        val time = StringBuffer()
        val addPop = LayoutInflater.from(this).inflate(R.layout.add_remind_popup, null, false)
        val addWindow = PopupWindow(addPop, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT)
        val simpleFormate = SimpleDateFormat("yyyy-M-dd")
        val date = Date(System.currentTimeMillis())
        addPop.tv_show_date_add_remind_pop.text = simpleFormate.format(date)
        addPop.iv_back_add_remind_pop.setOnClickListener { addWindow.dismiss() }
        addPop.iv_save_add_remind_pop.setOnClickListener {
            saveRemind(addWindow, addPop)
        }
        addWindow.setOnDismissListener {
            rv_remind.adapter.notifyItemChanged(list.size)
        }

        addPop.cv_date_remind_pop.setOnDateChangeListener { view, year, month, dayOfMonth ->
            this.date = "" + year + month + dayOfMonth
            addPop.tv_show_date_add_remind_pop.text = "$year-$month-$dayOfMonth"
        }
        addWindow.isFocusable = true
        addWindow.isOutsideTouchable = false
        addWindow.isTouchable = true
        addWindow.update()
        addWindow.softInputMode = PopupWindow.INPUT_METHOD_FROM_FOCUSABLE
        addWindow.showAtLocation(this.window.decorView, Gravity.TOP, 0, 0)
    }

    private fun saveRemind(addWindow: PopupWindow, addPop: View) {
        if (addPop.et_add_remind_pop.text.toString().isEmpty() || addPop.tv_show_date_add_remind_pop.text.toString().isEmpty()) {
            showToast.shortToast("不可以为空")
        } else {
            if (remindDbManager.addRemind(addPop.et_add_remind_pop.text.toString(), addPop.tv_show_date_add_remind_pop.text.toString())) {
                initAdapter()
                showToast.shortToast("添加成功")
            } else {
                showToast.shortToast("添加失败")
            }
            addWindow.dismiss()

        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == 0) {
            for (grant in grantResults) {
                if (grant != PackageManager.PERMISSION_GRANTED) {
                    openAppDetails()
                } else {

                }
            }
        }
    }

    private fun openAppDetails() {
        val builder = AlertDialog.Builder(this)
        builder.setMessage("拍照需要申请相机和存储权限”，请到 “应用信息 -> 权限” 中授予！")
        builder.setPositiveButton("去手动授权", DialogInterface.OnClickListener { dialog, which ->
            val intent = Intent()
            intent.action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
            intent.addCategory(Intent.CATEGORY_DEFAULT)
            intent.data = Uri.parse("package:$packageName")
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY)
            intent.addFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS)
            startActivity(intent)
        })
        builder.setNegativeButton("取消", null)
        builder.show()
    }

    fun TextView.showDate(activity: Activity, handler: Handler) {
        val time = StringBuffer()
        setBackGroundAlpha.set(activity, 0.3f)
        val calendar: Calendar = Calendar.getInstance()
        calendar.add(Calendar.MONTH, 1)
        var year1 = calendar.get(Calendar.YEAR)
        var month1 = calendar.get(Calendar.MONTH)
        var day1 = calendar.get(Calendar.DAY_OF_MONTH)
        val contentView: View = LayoutInflater.from(activity).inflate(R.layout.datepopup, null, false)
        val dateWindow = PopupWindow(contentView, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT, true)
        contentView.dp_date_popup.init(contentView.dp_date_popup.year, contentView.dp_date_popup.month, contentView.dp_date_popup.dayOfMonth
        ) { view, year, monthOfYear, dayOfMonth ->
            year1 = year
            month1 = monthOfYear + 1
            day1 = dayOfMonth
        }
        val msg = Message()
        msg.what = 1
        contentView.btn_confirm_date_popup.setOnClickListener {
            time.append(year1)
            time.append("-")
            time.append(month1)
            time.append("-")
            time.append(day1)
            text = time.toString()
            SpUtils.spSetDate(time.toString())
            date = time.toString()
            Log.d("date", date)
            dateWindow.dismiss()
            setBackGroundAlpha.set(activity, 1.0f)
            handler.sendMessage(msg)
        }
        contentView.btn_cancel_date_popup.setOnClickListener {
            dateWindow.dismiss()
            setBackGroundAlpha.set(activity, 1.0f)
        }

        dateWindow.setOnDismissListener {
            setBackGroundAlpha.set(activity, 1.0f)
            time.delete(0, time.length)
            SpUtils.spSetDate("")
        }
        dateWindow.isOutsideTouchable = false
        dateWindow.isTouchable = true
        dateWindow.update()
        dateWindow.showAtLocation(activity.window.decorView, Gravity.BOTTOM, 0, 0)
    }

}
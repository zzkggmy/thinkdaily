package com.byren.kai.thinkdaily.activity

import android.annotation.SuppressLint
import android.app.Activity
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.support.annotation.RequiresApi
import android.support.v7.widget.GridLayoutManager
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.WindowManager
import android.widget.LinearLayout
import android.widget.PopupWindow
import android.widget.TextView
import android.widget.Toast
import com.byren.kai.thinkdaily.R
import com.byren.kai.thinkdaily.adapter.ExpenditureTypeAdapter
import com.byren.kai.thinkdaily.adapter.IncomeTypeAdapter
import com.byren.kai.thinkdaily.utils.*
import com.byren.kai.thinkdaily.utils.setBackGroundAlpha.set
import kotlinx.android.synthetic.main.activity_addbill.*
import kotlinx.android.synthetic.main.datepopup.view.*
import kotlinx.android.synthetic.main.note_popup.*
import kotlinx.android.synthetic.main.note_popup.view.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

/**
 * Created by Lucas on 2018/3/27 .
 * 添加记账Activity
 */
class AddBillActivity : BaseActivity(), View.OnClickListener {
    private var date = ""
    private var id = 0
    private var expenditureType = "0"
    private var note = ""
    private var incomeType = "0"
    private var amount: Double = 0.00
    private var time = 1
    private val incomeTypeList: ArrayList<String> = ArrayList()
    private val expenditureList: ArrayList<String> = ArrayList()
    private val buffer: StringBuffer = StringBuffer()
    private var amountList: ArrayList<String> = ArrayList()
    private val incometypeGridLayoutManager: GridLayoutManager = GridLayoutManager(this, 6)
    private val expendituretypeGridLayoutManager: GridLayoutManager = GridLayoutManager(this, 6)
    @SuppressLint("SimpleDateFormat", "SetTextI18n", "ResourceAsColor")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        id = intent.getIntExtra("id", 0)
        setContentView(R.layout.activity_addbill)
        setSupportActionBar(tb_addbill)
        if (intent.getIntExtra("id", 0) == 0) {
            initStatusBar()
            val simpleFormate = SimpleDateFormat("yyyy-MM-dd")
            val date = Date(System.currentTimeMillis())
            tv_date_addbill.text = "" + simpleFormate.format(date)
            SpUtils.spSetDate(tv_date_addbill.text.toString())
        } else {
            initStatusBar()
            if (SpUtils.type() == 1) {
                note = incomeDbManager.modifyBill(id).incomeNote
                tv_amount_addbill.text = incomeDbManager.modifyBill(id).incomeAmount
                tv_date_addbill.text = incomeDbManager.modifyBill(id).incomeDate
                setIncomeIcon(incomeDbManager.modifyBill(id).incomeType.toInt())
            } else {
                note = expenditureDbManager.modifyBill(id).expenditureNote
                tv_amount_addbill.text = expenditureDbManager.modifyBill(id).expenditureAmount
                tv_date_addbill.text = expenditureDbManager.modifyBill(id).expenditureDate
                setExpenditureIcon(expenditureDbManager.modifyBill(id).expenditureType.toInt())
            }
        }
        for (i in 0..10) {
            incomeTypeList.add("" + i)
        }
        for (i in 0..16) {
            expenditureList.add("" + i)
        }
        incometypeGridLayoutManager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
            override fun getSpanSize(position: Int): Int {
                return 1
            }
        }
        expendituretypeGridLayoutManager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
            override fun getSpanSize(position: Int): Int {
                return 1
            }
        }
        rv_expenditure_addbill.adapter = ExpenditureTypeAdapter(this, expenditureList, { view, position ->
            setExpenditureIcon(position)
        })
        rv_expenditure_addbill.layoutManager = expendituretypeGridLayoutManager
        rv_expenditure_addbill.adapter.notifyDataSetChanged()
        rv_income_addbill.adapter = IncomeTypeAdapter(this, incomeTypeList, { position ->
            setIncomeIcon(position)
        })
        rv_income_addbill.layoutManager = incometypeGridLayoutManager
        rv_income_addbill.adapter.notifyDataSetChanged()
        iv_addbill_back.setOnClickListener(this)
        tv_income_addbill.setOnClickListener(this)
        tv_expenditure_addbill.setOnClickListener(this)
        tv_date_addbill.setOnClickListener(this)
        tv_note_addbill.setOnClickListener(this)
        tv_0_addbill.setOnClickListener(this)
        tv_1_addbill.setOnClickListener(this)
        tv_2_addbill.setOnClickListener(this)
        tv_3_addbill.setOnClickListener(this)
        tv_4_addbill.setOnClickListener(this)
        tv_5_addbill.setOnClickListener(this)
        tv_6_addbill.setOnClickListener(this)
        tv_7_addbill.setOnClickListener(this)
        tv_8_addbill.setOnClickListener(this)
        tv_9_addbill.setOnClickListener(this)
        tv_point_addbill.setOnClickListener(this)
        iv_delete_addbill.setOnClickListener(this)
        tv_clear_addbill.setOnClickListener(this)
        iv_ok_addbill.setOnClickListener(this)
        iv_plus_addbill.setOnClickListener(this)
        iv_save_add_bill.setOnClickListener(this)
    }

    private fun initStatusBar() {
        if (SpUtils.type() == 1) {
            tv_income_addbill.setTextColor(resources.getColor(R.color.orange))
            tv_expenditure_addbill.setTextColor(resources.getColor(R.color.white))
            rv_expenditure_addbill.visibility = View.GONE
            rv_income_addbill.visibility = View.VISIBLE
            iv_type_addbill.setImageResource(R.drawable.pigbank)
            rl_show_addbill.setBackgroundColor(resources.getColor(R.color.green))
        } else {
            tv_expenditure_addbill.setTextColor(resources.getColor(R.color.orange))
            tv_income_addbill.setTextColor(resources.getColor(R.color.white))
            rv_expenditure_addbill.visibility = View.VISIBLE
            rv_income_addbill.visibility = View.GONE
            iv_type_addbill.setImageResource(R.drawable.normal)
            rl_show_addbill.setBackgroundColor(resources.getColor(R.color.orange))
        }
    }

    private fun setIncomeIcon(position: Int) {
        when (position) {
            0 -> {
                iv_type_addbill.setImageResource(R.drawable.pigbank)
                incomeType = "0"
            }
            1 -> {
                iv_type_addbill.setImageResource(R.drawable.wage)
                incomeType = "1"
            }

            2 -> {
                iv_type_addbill.setImageResource(R.drawable.borrow)
                incomeType = "2"
            }
            3 -> {
                iv_type_addbill.setImageResource(R.drawable.bonus)
                incomeType = "3"
            }

            4 -> {
                iv_type_addbill.setImageResource(R.drawable.redb)
                incomeType = "4"
            }

            5 -> {
                iv_type_addbill.setImageResource(R.drawable.reim)
                incomeType = "5"
            }

            6 -> {
                iv_type_addbill.setImageResource(R.drawable.invest)
                incomeType = "6"
            }

            7 -> {
                iv_type_addbill.setImageResource(R.drawable.interest)
                incomeType = "7"
            }

            8 -> {
                iv_type_addbill.setImageResource(R.drawable.lottery)
                incomeType = "8"
            }

            9 -> {
                iv_type_addbill.setImageResource(R.drawable.other)
                incomeType = "9"
            }

        }
    }

    private fun setExpenditureIcon(position: Int) {
        when (position) {
            0 -> {
                expenditureType = "0"
                iv_type_addbill.setImageResource(R.drawable.normal)
            }
            1 -> {
                expenditureType = "1"
                iv_type_addbill.setImageResource(R.drawable.dining)
            }
            2 -> {
                expenditureType = "2"
                iv_type_addbill.setImageResource(R.drawable.traffic)
            }
            3 -> {
                expenditureType = "3"
                iv_type_addbill.setImageResource(R.drawable.shop)
            }
            4 -> {
                expenditureType = "4"
                iv_type_addbill.setImageResource(R.drawable.apparel)
            }
            5 -> {
                expenditureType = "5"
                iv_type_addbill.setImageResource(R.drawable.house)
            }
            6 -> {
                expenditureType = "6"
                iv_type_addbill.setImageResource(R.drawable.dailyuse)
            }
            7 -> {
                expenditureType = "7"
                iv_type_addbill.setImageResource(R.drawable.cosmetic)
            }
            8 -> {
                expenditureType = "8"
                iv_type_addbill.setImageResource(R.drawable.elec)
            }
            9 -> {
                expenditureType = "9"
                iv_type_addbill.setImageResource(R.drawable.snacks)
            }
            10 -> {
                expenditureType = "10"
                iv_type_addbill.setImageResource(R.drawable.edu)
            }
            11 -> {
                expenditureType = "11"
                iv_type_addbill.setImageResource(R.drawable.commun)
            }
            12 -> {
                expenditureType = "12"
                iv_type_addbill.setImageResource(R.drawable.medical)
            }
            13 -> {
                expenditureType = "13"
                iv_type_addbill.setImageResource(R.drawable.travel)
            }
            14 -> {
                expenditureType = "14"
                iv_type_addbill.setImageResource(R.drawable.humanity)
            }
            15 -> {
                expenditureType = "15"
                iv_type_addbill.setImageResource(R.drawable.other)
            }

        }
    }

    private val handler: Handler = @SuppressLint("HandlerLeak")
    object : Handler() {
        override fun handleMessage(msg: Message?) {
            super.handleMessage(msg)
            when (msg!!.what) {
                1 -> {
                    tv_amount_addbill.text = buffer
                }
                2 -> {
                    tv_amount_addbill.text = ""
                    amount = 0.00
                }
            }
        }
    }

    override fun onClick(v: View?) {
        val msg = Message()
        msg.what = 1
        when (v!!.id) {
            R.id.iv_addbill_back -> finish()
            R.id.tv_income_addbill -> {
                rv_income_addbill.visibility = View.VISIBLE
                rv_expenditure_addbill.visibility = View.GONE
                iv_type_addbill.setImageResource(R.drawable.pigbank)
                rl_show_addbill.setBackgroundColor(resources.getColor(R.color.green))
                tv_income_addbill.setTextColor(resources.getColor(R.color.orange))
                tv_expenditure_addbill.setTextColor(Color.WHITE)
                SpUtils.spSetType(1)
            }
            R.id.tv_expenditure_addbill -> {
                rv_expenditure_addbill.visibility = View.VISIBLE
                rv_income_addbill.visibility = View.GONE
                iv_type_addbill.setImageResource(R.drawable.normal)
                rl_show_addbill.setBackgroundColor(resources.getColor(R.color.orange))
                tv_income_addbill.setTextColor(Color.WHITE)
                tv_expenditure_addbill.setTextColor(resources.getColor(R.color.orange))
                SpUtils.spSetType(2)
            }
            R.id.tv_date_addbill -> {
                tv_date_addbill.showDate(this)
            }
            R.id.tv_note_addbill -> showNotePopup()
            R.id.tv_0_addbill -> {
                buffer.append(0)
                handler.sendMessage(msg)
            }
            R.id.tv_1_addbill -> {
                buffer.append(1)
                handler.sendMessage(msg)
            }
            R.id.tv_2_addbill -> {
                buffer.append(2)
                handler.sendMessage(msg)
            }
            R.id.tv_3_addbill -> {
                buffer.append(3)
                handler.sendMessage(msg)
            }
            R.id.tv_4_addbill -> {
                buffer.append(4)
                handler.sendMessage(msg)
            }
            R.id.tv_5_addbill -> {
                buffer.append(5)
                handler.sendMessage(msg)
            }
            R.id.tv_6_addbill -> {
                buffer.append(6)
                handler.sendMessage(msg)
            }
            R.id.tv_7_addbill -> {
                buffer.append(7)
                handler.sendMessage(msg)
            }
            R.id.tv_8_addbill -> {
                buffer.append(8)
                handler.sendMessage(msg)
            }
            R.id.tv_9_addbill -> {
                buffer.append(9)
                handler.sendMessage(msg)
            }
            R.id.tv_point_addbill -> {
                buffer.append(".")
                handler.sendMessage(msg)
            }
            R.id.iv_delete_addbill -> {
                Log.d("bu", "" + buffer.length)
                if (buffer.length - 1 >= 0) {
                    buffer.deleteCharAt(buffer.length - 1)
                    handler.sendMessage(msg)
                }
            }

            R.id.iv_plus_addbill -> {
                amountList.add(tv_amount_addbill.text.toString())
                Log.d("b", tv_amount_addbill.text.toString())
                msg.what = 2
                iv_ok_addbill.setImageResource(R.drawable.equal)
                time = 2
                buffer.delete(0, buffer.length)
            }
            R.id.iv_ok_addbill -> {
                calculate()
            }
            R.id.tv_clear_addbill -> {
                tv_amount_addbill.text = ""
                buffer.delete(0, buffer.length)
                amountList.clear()
            }
            R.id.iv_save_add_bill -> {
                if (id == 0) {
                    saveBill()

                } else {
                    modifyBillInfo()
                }
            }

        }

    }

    private fun modifyBillInfo() {
        if (SpUtils.type() == 1) {
            if (incomeDbManager.modifyBillInfo(id,
                            tv_amount_addbill.text.toString(), incomeType,
                            tv_date_addbill.text.toString(), note)) {
                Log.d("id", "" + id)
                showToast.shortToast("修改成功")
                finish()
            } else {
                showToast.shortToast("修改失败")
            }
        } else {
            if (expenditureDbManager.modifyBillInfo(id, tv_amount_addbill.text.toString()
                            , expenditureType, tv_date_addbill.text.toString(), note)) {
                showToast.shortToast("修改成功")
                finish()
            } else {
                showToast.shortToast("修改失败")
            }
        }
    }

    private fun saveBill() {
        if (tv_amount_addbill.text == "0.00") {
            showToast.shortToast("金额不可为空")
        } else {
            if (SpUtils.type() == 1) {
                if (incomeDbManager.addIncome(incomeType, tv_amount_addbill.text.toString(),
                                tv_date_addbill.text.toString(), note)) {
                    showToast.shortToast("插入成功")
                    finish()
                } else {
                    showToast.shortToast("插入失败")
                }

            } else if (SpUtils.type() == 2) {
                if (expenditureDbManager.addExpenditure(expenditureType, tv_amount_addbill.text.toString(),
                                tv_date_addbill.text.toString(), note)) {
                    finish()
                } else {
                    showToast.shortToast("插入失败")
                }
            }
        }
    }

    private fun calculate() {
        iv_ok_addbill.setImageResource(R.drawable.ok)
        amountList.add(tv_amount_addbill.text.toString())
        Log.d("d", tv_amount_addbill.text.toString())
        if (amountList.size > 0) {
            for (i in 0 until amountList.size) {
                if (amountList.get(i) == ".") {
                    Toast.makeText(this, "请输入正确的金额", Toast.LENGTH_SHORT).show()
                } else {
                    amount += amountList.get(i).toDouble()
                }
            }
        }
        tv_amount_addbill.text = "" + amount
        amountList.clear()
        amount = 0.00
        time = 1
    }

    private fun showNotePopup() {
        val contentView: View = LayoutInflater.from(this).inflate(R.layout.note_popup, null, false)
        val noteWindow = PopupWindow(contentView, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT, true)
        noteWindow.softInputMode = WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN
        if (intent.getIntExtra("id", 0) != 0) {
            if (SpUtils.type() == 1) {
                contentView.et_note_popup.setText(incomeDbManager.modifyBill(id).incomeNote)
            } else {
                contentView.et_note_popup.setText(expenditureDbManager.modifyBill(id).expenditureNote)
            }
        }
        set(this, 0.3f)
        contentView.tv_date_notepop.text = SpUtils.date()
        contentView.tv_comlplete_notepop.setOnClickListener {
            note = contentView.et_note_popup.text.toString()
            noteWindow.dismiss()
            set(this, 1.0f)
        }
        noteWindow.setOnDismissListener {
            set(this, 1.0f)
        }
        contentView.et_note_popup.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                if (s!!.length > 100) {
                    Toast.makeText(this@AddBillActivity, "最大字数为100字", Toast.LENGTH_SHORT).show()
                    s.delete(100, s.length)
                    contentView.tv_num_notepop.text = "" + s.length + "/100"
                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            @SuppressLint("SetTextI18n")
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                contentView.tv_num_notepop.text = s!!.length.toString() + "/100"
            }

        })
        noteWindow.isOutsideTouchable = false
        noteWindow.isTouchable = true
        noteWindow.update()
        noteWindow.showAtLocation(this.window.decorView, Gravity.BOTTOM, 0, 0)
    }


    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onResume() {
        super.onResume()
        setImmersiveStatusBar(tb_addbill, this)
    }

    fun TextView.showDate(activity: Activity) {
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
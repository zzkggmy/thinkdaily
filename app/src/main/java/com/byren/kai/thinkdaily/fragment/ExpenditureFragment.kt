package com.byren.kai.thinkdaily.fragment

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentActivity
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.helper.ItemTouchHelper
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.PopupWindow
import android.widget.TextView
import com.byren.kai.thinkdaily.R
import com.byren.kai.thinkdaily.activity.AddBillActivity
import com.byren.kai.thinkdaily.adapter.ExpenditureAdapter
import com.byren.kai.thinkdaily.adapter.ExpenditureTypePopAdapter
import com.byren.kai.thinkdaily.utils.*
import kotlinx.android.synthetic.main.datepopup.view.*
import kotlinx.android.synthetic.main.expenditure_pop.view.*
import kotlinx.android.synthetic.main.fragment_expenditure.view.*
import kotlinx.android.synthetic.main.fragment_income.view.*
import java.util.*
import javax.xml.transform.Result

class ExpenditureFragment : Fragment(), View.OnClickListener {
    private var handler: Handler? = null
    private var expenditureType = -1
    private var expenditureDate = ""
    private var adapter: ExpenditureAdapter? = null
    private val mContext = Common.mContext()
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = View.inflate(activity!!, R.layout.fragment_expenditure, null)
//        initAdapter(view)
        view.ll_classification_expenditure_frag.setOnClickListener(this)
        view.ll_date_expenditure_frag.setOnClickListener(this)
        view.iv_complete_expenditure_frag.setOnClickListener(this)
        handler = @SuppressLint("HandlerLeak")
        object : Handler() {
            override fun handleMessage(msg: Message?) {
                super.handleMessage(msg)
                when (msg!!.what) {
                    1 -> {
                        if (expenditureType >= 0) {
                            view.iv_complete_expenditure_frag.visibility = View.VISIBLE
                        } else if (expenditureDate != "日期") {
                            view.iv_complete_expenditure_frag.visibility = View.VISIBLE
                        } else {
                            view.iv_complete_expenditure_frag.visibility = View.GONE
                        }
                    }
                }
            }
        }
        return view
    }


    private fun initAdapter(view: View) {
        adapter = ExpenditureAdapter(activity!!, expenditureDbManager.getExpenditure(), { position ->
            SpUtils.spSetType(2)
            val intent = Intent(activity, AddBillActivity::class.java)
            intent.putExtra("id", expenditureDbManager.getExpenditure().get(position).expenditureId)
            startActivity(intent)
        })
        val itemTouchHelper = ItemTouchHelper(AdapterItemTouchHelper(adapter))
        itemTouchHelper.attachToRecyclerView(view.rv_expenditure_frag)
        view.rv_expenditure_frag.adapter = adapter
        view.rv_expenditure_frag.layoutManager = LinearLayoutManager(activity)
        view.rv_expenditure_frag.adapter!!.notifyDataSetChanged()
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.ll_classification_expenditure_frag -> {
                show(activity!!, v.iv_classification_expenditure_frag, v.tv_classification_expenditure_frag)
                setBackGroundAlpha.set(activity!!, 0.3f)
            }
            R.id.ll_date_expenditure_frag -> {
                v.tv_date_expenditure_frag.showDate(activity!!,handler!!)
            }
            R.id.iv_complete_expenditure_frag -> {
                adapter = ExpenditureAdapter(activity!!, expenditureDbManager.conditionQuery(expenditureType.toString(), expenditureDate), { position ->
                    SpUtils.spSetType(2)
                    val intent = Intent(activity, AddBillActivity::class.java)
                    intent.putExtra("id", expenditureDbManager.getExpenditure().get(position).expenditureId)
                    startActivity(intent)
                })
                val itemTouchHelper = ItemTouchHelper(AdapterItemTouchHelper(adapter))
                itemTouchHelper.attachToRecyclerView(view!!.rv_expenditure_frag)
                view!!.rv_expenditure_frag.adapter = adapter
                view!!.rv_expenditure_frag.layoutManager = LinearLayoutManager(activity)
                view!!.rv_expenditure_frag.adapter!!.notifyDataSetChanged()
            }
        }
    }

    override fun onResume() {
        super.onResume()
        adapter = ExpenditureAdapter(activity!!, expenditureDbManager.getExpenditure(), { position ->
            SpUtils.spSetType(2)
            val intent = Intent(activity, AddBillActivity::class.java)
            intent.putExtra("id", expenditureDbManager.getExpenditure().get(position).expenditureId)
            startActivity(intent)
        })
        val itemTouchHelper = ItemTouchHelper(AdapterItemTouchHelper(adapter))
        itemTouchHelper.attachToRecyclerView(view!!.rv_expenditure_frag)
        view!!.rv_expenditure_frag.adapter = adapter
        view!!.rv_expenditure_frag.layoutManager = LinearLayoutManager(activity)
        view!!.rv_expenditure_frag.adapter!!.notifyDataSetChanged()
    }

    fun show(activity: Activity, iv: ImageView, tv: TextView) {
        val expenditureList: ArrayList<String> = ArrayList()
        val gridLayoutManager = GridLayoutManager(activity, 4)
        gridLayoutManager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
            override fun getSpanSize(position: Int): Int {
                return 1
            }
        }
        for (i in 0..16) {
            expenditureList.add("" + i)
        }
        val msg = Message()
        msg.what = 1
        val view: View = LayoutInflater.from(getActivity()).inflate(R.layout.expenditure_pop, null, false)
        val window = PopupWindow(view, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT)
        view.rv_expenditure_pop.adapter = ExpenditureTypePopAdapter(activity, expenditureList, { view, position ->
            setIcon(position, iv, tv)
            expenditureType = position
            window.dismiss()
            handler!!.sendMessage(msg)
        })
        window.setOnDismissListener {
            setBackGroundAlpha.set(activity, 1.0f)
        }
        view.rv_expenditure_pop.layoutManager = gridLayoutManager
        view.rv_expenditure_pop.adapter.notifyDataSetChanged()
        window.isOutsideTouchable = true
        window.isTouchable = true
        window.update()
        window.showAtLocation(activity.window.decorView, Gravity.BOTTOM, 0, 0)
    }

    private fun setIcon(position: Int, iv: ImageView, tv: TextView) {
        when (position) {
            0 -> {
                iv.setImageResource(R.drawable.normal)
                tv.text = "一般"
            }
            1 -> {
                iv.setImageResource(R.drawable.dining)
                tv.text = "用餐"
            }
            2 -> {
                iv.setImageResource(R.drawable.traffic)
                tv.text = "交通"
            }
            3 -> {
                iv.setImageResource(R.drawable.shop)
                tv.text = "购物"
            }
            4 -> {
                iv.setImageResource(R.drawable.apparel)
                tv.text = "服饰"
            }
            5 -> {
                iv.setImageResource(R.drawable.house)
                tv.text = "住房"
            }
            6 -> {
                iv.setImageResource(R.drawable.dailyuse)
                tv.text = "日用品"
            }
            7 -> {
                iv.setImageResource(R.drawable.cosmetic)
                tv.text = "化妆品"
            }
            8 -> {
                iv.setImageResource(R.drawable.elec)
                tv.text = "电子产品"
            }
            9 -> {
                iv.setImageResource(R.drawable.snacks)
                tv.text = "零食"
            }
            10 -> {
                iv.setImageResource(R.drawable.edu)
                tv.text = "教育"
            }
            11 -> {
                iv.setImageResource(R.drawable.commun)
                tv.text = "通讯"
            }
            12 -> {
                iv.setImageResource(R.drawable.medical)
                tv.text = "医疗"
            }
            13 -> {
                iv.setImageResource(R.drawable.travel)
                tv.text = "旅游"
            }
            14 -> {
                iv.setImageResource(R.drawable.humanity)
                tv.text = "人情"
            }

            15 -> {
                iv.setImageResource(R.drawable.other)
                tv.text = "其他"
            }
        }
    }

    fun TextView.showDate(activity: Activity, handler: Handler) {
        val time = StringBuffer()
        setBackGroundAlpha.set(activity, 0.3f)
        val calendar: Calendar = Calendar.getInstance()
        calendar.add(Calendar.MONTH, 1)
        var year1 = calendar.get(Calendar.YEAR)
        var month1 = calendar.get(Calendar.MONTH)
        var day1 = calendar.get(Calendar.DAY_OF_MONTH)
        val msg = Message()
        msg.what = 1
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
            expenditureDate = time.toString()
            Log.d("date", expenditureDate)
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
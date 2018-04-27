package com.byren.kai.thinkdaily.activity

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.support.annotation.RequiresApi
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.helper.ItemTouchHelper
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.WindowManager
import android.widget.LinearLayout
import android.widget.PopupWindow
import com.byren.kai.thinkdaily.R
import com.byren.kai.thinkdaily.adapter.QuickAdapter
import com.byren.kai.thinkdaily.beans.Quick
import com.byren.kai.thinkdaily.utils.*
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_remind.*
import kotlinx.android.synthetic.main.quick_add_pop.view.*
import kotlinx.android.synthetic.main.settext_popup.view.*

class MainActivity : BaseActivity(), View.OnClickListener {
    private var quickList: ArrayList<Quick> = ArrayList()
    private var adapter: QuickAdapter? = null
    private val linearLayoutManager = LinearLayoutManager(this)
    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        tv_set_income_main.text = SpUtils.income()
        tv_set_budget_main.text = SpUtils.budge()
        tv_set_income_main.setOnClickListener(this)
        tv_set_budget_main.setOnClickListener(this)
        iv_add_main.setOnClickListener(this)
        iv_remind_main.setOnClickListener(this)
        iv_statistics_main.setOnClickListener(this)
        iv_quick_write.setOnClickListener(this)
        tv_expenditure_main.text = "¥" + expenditureDbManager.getExpenditureNum()
        initAdapter()
        quickList = quickDbManager.getQuickDb()

    }

    private fun initAdapter() {
        adapter = QuickAdapter(this, quickDbManager.getQuickDb())
        val helper = AdapterItemTouchHelper(adapter)
        val itemTouchHelper = ItemTouchHelper(helper)
        itemTouchHelper.attachToRecyclerView(rv_quick_main)
        rv_quick_main.adapter = adapter
        rv_quick_main.layoutManager = linearLayoutManager
        adapter!!.notifyDataSetChanged()
    }

    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.tv_set_income_main -> {
                setBackGroundAlpha.set(this, 0.3f)
                val popView: View = LayoutInflater.from(this).inflate(R.layout.settext_popup, null, false)
                val ppWindow = PopupWindow(popView, 800, 200, true)
                popView.tv_entry_set_text_popup.setOnClickListener {
                    SpUtils.spSetIncome("" + popView.et_set_text_popup.text.toString())
                    tv_set_income_main.text = SpUtils.income()
                    ppWindow.dismiss()
                    setBackGroundAlpha.set(this, 1.0f)
                }
                ppWindow.setOnDismissListener {
                    setBackGroundAlpha.set(this, 1.0f)
                }
                ppWindow.isOutsideTouchable = false
                ppWindow.isTouchable = true
                ppWindow.update()
                ppWindow.showAtLocation(this.window.decorView, Gravity.CENTER, 0, 0)
            }
            R.id.tv_set_budget_main -> {
                setBackGroundAlpha.set(this, 0.3f)
                val popView: View = LayoutInflater.from(this).inflate(R.layout.settext_popup, null, false)
                val ppWindow = PopupWindow(popView, 800, 200, true)
                popView.tv_entry_set_text_popup.setOnClickListener {
                    SpUtils.spSetBudge("" + popView.et_set_text_popup.text.toString())
                    tv_set_budget_main.text = SpUtils.budge()
                    ppWindow.dismiss()
                    setBackGroundAlpha.set(this, 1.0f)
                }
                ppWindow.setOnDismissListener {
                    setBackGroundAlpha.set(this, 1.0f)
                }
                ppWindow.isFocusable = true
                ppWindow.isOutsideTouchable = false
                ppWindow.isTouchable = true
                ppWindow.update()
                ppWindow.softInputMode = PopupWindow.INPUT_METHOD_FROM_FOCUSABLE
                ppWindow.showAtLocation(this.window.decorView, Gravity.CENTER, 0, 0)
            }
            R.id.iv_add_main -> {
                val intent = Intent(this, AddBillActivity::class.java)
                intent.putExtra("id", 0)
                startActivity(intent)
            }
            R.id.iv_remind_main -> startActivity(Intent(this, RemindActivity::class.java))
            R.id.iv_statistics_main -> startActivity(Intent(this, StatisticsActivity::class.java))
            R.id.iv_quick_write -> showQuickPop()
        }
    }

    private fun showQuickPop() {
        setBackGroundAlpha.set(this, 0.3f)
        val contentView = LayoutInflater.from(this).inflate(R.layout.quick_add_pop, null, false)
        val quickWindow = PopupWindow(contentView, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT)
        quickWindow.softInputMode = WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE
        contentView.iv_back_quick_pop.setOnClickListener {
            quickWindow.dismiss()
            setBackGroundAlpha.set(this, 1.0f)
        }
        contentView.tv_commit_quick_pop.setOnClickListener {
            if (contentView.et_quick_add_pop.text.toString().isEmpty()) {
                showToast.shortToast("不可为空")
            } else {
                quickDbManager.addQuick(contentView.et_quick_add_pop.text.toString())
                initAdapter()
                quickWindow.dismiss()
                setBackGroundAlpha.set(this, 1.0f)
                quickWindow.setOnDismissListener {
                    setBackGroundAlpha.set(this, 1.0f)
                }
            }
        }
        quickWindow.setOnDismissListener {
            setBackGroundAlpha.set(this, 1.0f)
        }
        quickWindow.isFocusable = true
        quickWindow.isOutsideTouchable = false
        quickWindow.isTouchable = true
        quickWindow.update()
        quickWindow.softInputMode = PopupWindow.INPUT_METHOD_FROM_FOCUSABLE
        quickWindow.showAtLocation(this.window.decorView, Gravity.BOTTOM, 0, 0)
    }

    @SuppressLint("SetTextI18n")
    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onResume() {
        super.onResume()
        setImmersiveStatusBar(tb_main, this)
        tv_expenditure_main.text = "¥" + expenditureDbManager.getExpenditureNum()
    }
}

package com.byren.kai.thinkdaily.activity

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.RectF
import android.os.Build
import android.support.annotation.RequiresApi
import android.util.AttributeSet
import android.util.Log
import android.view.View
import com.byren.kai.thinkdaily.R
import com.byren.kai.thinkdaily.utils.Common
import com.byren.kai.thinkdaily.utils.ScreenSizeUtils
import com.byren.kai.thinkdaily.utils.expenditureDbManager

@RequiresApi(Build.VERSION_CODES.LOLLIPOP)
@SuppressLint("ViewConstructor")
class PieView : View {
    constructor(context: Context) : super(context)
    constructor(context: Context,attributeSet: AttributeSet) : super(context,attributeSet)


    private var lengthX: Int = 0
    private var lengthY: Int = 0
    private var radius: Float = 0.00f
    private var rectf: RectF = RectF()
    private val whitePaint = Paint()
    private val redPaint = Paint()
    private val greenPaint = Paint()
    private val zhihuPaint = Paint()
    private val blackPaint = Paint()
    private val orangePaint = Paint()
    init {
        whitePaint.color = resources.getColor(R.color.white)
        whitePaint.style = Paint.Style.FILL
        whitePaint.isAntiAlias = true
        whitePaint.isDither = true

        redPaint.color = resources.getColor(R.color.red)
        redPaint.style = Paint.Style.FILL
        redPaint.isAntiAlias = true
        redPaint.isDither = true

        greenPaint.color = resources.getColor(R.color.green)
        greenPaint.style = Paint.Style.FILL
        greenPaint.isAntiAlias = true
        greenPaint.isDither = true

        zhihuPaint.color = resources.getColor(R.color.zhihu_primary)
        zhihuPaint.style = Paint.Style.FILL
        zhihuPaint.isAntiAlias = true
        zhihuPaint.isDither = true

        blackPaint.color = resources.getColor(R.color.black)
        blackPaint.style = Paint.Style.FILL
        blackPaint.isAntiAlias = true
        blackPaint.isDither = true

        orangePaint.color = resources.getColor(R.color.orange)
        orangePaint.style = Paint.Style.FILL
        orangePaint.isAntiAlias = true
        orangePaint.isDither = true
    }


    @SuppressLint("DrawAllocation", "ResourceAsColor")
    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        var width = MeasureSpec.getSize(widthMeasureSpec)
        var height = MeasureSpec.getSize(heightMeasureSpec)
        val widthMode = MeasureSpec.getMode(widthMeasureSpec)
        val heightMode = MeasureSpec.getMode(heightMeasureSpec)
        if (widthMode == MeasureSpec.AT_MOST || heightMode == MeasureSpec.AT_MOST) {
            width = ScreenSizeUtils.getScreenWidth()
            height = ScreenSizeUtils.getScreenHeight()
        }
        setMeasuredDimension(width, height)

    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        lengthX = w
        lengthY = h

        radius = ((Math.min(ScreenSizeUtils.getScreenWidth(),ScreenSizeUtils.getScreenHeight()) / 6) *0.8).toFloat()
        Log.d("w","" + radius)
        rectf.set(radius,radius,radius*5,radius*5)
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        canvas!!.drawArc(rectf,0f,90f,true,greenPaint)
        canvas.drawArc(rectf,90f,30f,true,redPaint)
        canvas.drawArc(rectf,120f,60f,true,zhihuPaint)
        canvas.drawArc(rectf,180f,20f,true,blackPaint)
        canvas.drawArc(rectf,200f,160f,true,orangePaint)
        canvas.drawCircle((radius * 3).toFloat(), (radius * 3).toFloat(), radius-20, whitePaint)
    }
}
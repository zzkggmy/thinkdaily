package com.byren.kai.thinkdaily.activity;

import android.content.Context;
        import android.graphics.Canvas;
        import android.graphics.Color;
        import android.graphics.Paint;
        import android.graphics.RectF;
        import android.util.AttributeSet;
        import android.util.Log;
        import android.view.MotionEvent;
        import android.view.View;


import com.byren.kai.thinkdaily.utils.ScreenSizeUtils;

import java.util.Arrays;


public class HollowPieNewChart extends View {



    private Paint redPaint = new Paint();
    public HollowPieNewChart(Context context) {
        super(context);
        init(context);
    }

    public HollowPieNewChart(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public HollowPieNewChart(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        redPaint.setColor(Color.RED);
        redPaint.setStyle(Paint.Style.FILL);
        redPaint.setAntiAlias(true);
        redPaint.setDither(true);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawPiePath(canvas);
    }

    private void drawPiePath(Canvas canvas) {
        //起始地角度
        float startAngle = 0;
        float sweepAngle = 360;//每个扇形的角度
        canvas.drawCircle(ScreenSizeUtils.INSTANCE.getScreenWidth()/2,ScreenSizeUtils.INSTANCE.getScreenHeight()/2,250f,redPaint);
        }
    }
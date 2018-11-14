package com.example.djd.fingertest;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.SweepGradient;
import android.util.AttributeSet;
import android.widget.ImageView;

/**
 * Created by djd on 18-11-14.
 */

public class CircleView extends ImageView {

    private static final int COLORS[]  =  new int[]{
            Color.parseColor("#F60C0C"),//红
            Color.parseColor("#F3B913"),//橙
            Color.parseColor("#E7F716"),//黄
            Color.parseColor("#3DF30B"),//绿
            Color.parseColor("#0DF6EF"),//青
            Color.parseColor("#0829FB"),//蓝
            Color.parseColor("#B709F4"),//紫
    };


    private Paint mPaint;


    public CircleView(Context context){
        super(context,null);
    }


    public CircleView(Context context, AttributeSet attributeSet){

        super(context, attributeSet);
        init();
    }


    private void init() {
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setStyle(Paint.Style.FILL);
    }


    @Override
    protected void onDraw(Canvas canvas) {
        //super.onDraw(canvas);
        int width = getWidth();
        int height = getHeight();
        int w = width / 2;
        int h = height / 2;
        float[] position = new float[]{ 1.f / 7, 2.f / 7, 3.f / 7, 4.f / 7, 5.f / 7, 6.f / 7, 1.f};
        int radius = w < h ? w : h;
        mPaint.setShader(new SweepGradient(w,h, COLORS, null));
        canvas.drawCircle(w,h,radius*1f, mPaint);

    }
}

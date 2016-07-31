package com.example.asus.tastenews.ui;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.util.AttributeSet;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ASUS on 2016/7/30.
 */
public class DynamicView extends View {
    private Paint mPaint;
    private List<Point>mTraces;

    public DynamicView(Context context){
        this(context,null);
    }
    public DynamicView(Context context,AttributeSet attrs){
        super(context,attrs);
        init();
    }

    private void init(){
        mPaint = new Paint();
        mPaint.setColor(Color.BLACK);
        mPaint.setAntiAlias(true);

        mTraces = new ArrayList<>();
    }

    @Override
    public void draw(Canvas canvas){
        super.draw(canvas);
    }
}

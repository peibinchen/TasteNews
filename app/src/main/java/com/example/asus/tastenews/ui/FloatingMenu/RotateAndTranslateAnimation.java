package com.example.asus.tastenews.ui.FloatingMenu;

import android.view.animation.Animation;
import android.view.animation.Transformation;

/**
 * Created by ASUS on 2016/7/21.
 */
public class RotateAndTranslateAnimation extends Animation {
    private float mFromXValue = 0.0f;
    private float mToXValue = 0.0f;
    private float mFromYValue = 0.0f;
    private float mToYValue = 0.0f;
    private float mFromDegrees;
    private float mToDegrees;

    private int mFromXType = ABSOLUTE;
    private int mToXType = ABSOLUTE;
    private int mFromYType = ABSOLUTE;
    private int mToYType = ABSOLUTE;

    private float mPivotXValue = 0.0f;
    private float mPivotYValue = 0.0f;
    private int mPivotXType = ABSOLUTE;
    private int mPivotYType = ABSOLUTE;
    private float mPivotX;//中心点x坐标
    private float mPivotY;//中心点y坐标

    private float mFromXDelta;
    private float mToXDelta;
    private float mFromYDelta;
    private float mToYDelta;

    public RotateAndTranslateAnimation(float fromXDelta,float toXDelta,float fromYDelta,float toYDelta,float fromDegrees,float toDegrees){
        mFromXValue = fromXDelta;
        mToXValue = toXDelta;
        mFromYValue = fromYDelta;
        mToYValue = toYDelta;

        mFromXType = ABSOLUTE;
        mToXType = ABSOLUTE;
        mFromYType = ABSOLUTE;
        mToYType = ABSOLUTE;

        mFromDegrees = fromDegrees;
        mToDegrees = toDegrees;

        /*
        (pivotX,pivotY)是旋转中心点
        RELATIVE_TO_SELF表示pivotXValue和pivotYValue都不是真正的像素数值或大小数值，
        只是一个比例，要与相应的长或宽相乘
         */
        mPivotXType = RELATIVE_TO_SELF;
        mPivotXValue = 0.5f;
        mPivotYType = RELATIVE_TO_SELF;
        mPivotYValue = 0.5f;
    }

    @Override
    protected void applyTransformation(float interpolatedTime, Transformation t){
        final float degrees = mFromDegrees + interpolatedTime * (mToDegrees - mFromDegrees);
        /*
        set方法会对之前所有的变化进行刷除，再进行变换
         */
        if(mPivotX == 0.0f && mPivotY == 0.0f){
            t.getMatrix().setRotate(degrees);
        }else{
            t.getMatrix().setRotate(degrees,mPivotX,mPivotY);
        }

        float dx = mFromXDelta;
        float dy = mFromYDelta;

        if(mFromXDelta != mToXDelta){
            dx += (mToXDelta - mFromXDelta) * interpolatedTime;
        }
        if(mFromYDelta != mToYDelta){
            dy += (mToYDelta - mFromYDelta) * interpolatedTime;
        }

        /*
        post方法则是向后生长的，也就是说是紧跟在前一个变化的后面
        关于post，set和pre的区别可以看泡在网上的日子的博客：
        http://www.jcodecraeer.com/a/anzhuokaifa/androidkaifa/2012/1115/561.html
         */
        t.getMatrix().postTranslate(dx,dy);
    }

    /**
     * initialize（）函数之后就会调用getTransform（）函数，在getTransform函数中就会调用到
     * applyTransform（）函数
     * @param width
     * @param height
     * @param parentWidth
     * @param parentHeight
     */
    @Override
    public void initialize(int width, int height, int parentWidth, int parentHeight) {
        super.initialize(width, height, parentWidth, parentHeight);
        /*
        计算出真实坐标，
        ABSOLUTE：mFromXDelta = mFromXValue
        RELATIVE_TO_SELF：mFromXDelta = mFromXValue * width
        RELATIVE_TO_PARENT：mFromXDelta = mFromXValue * parentWidth
         */
        mFromXDelta = resolveSize(mFromXType,mFromXValue,width,parentWidth);
        mToXDelta = resolveSize(mToXType,mToXValue,width,parentWidth);
        mFromYDelta = resolveSize(mFromYType,mFromYValue,height,parentHeight);
        mToYDelta = resolveSize(mToYType,mToYValue,height,parentHeight);

        mPivotX = resolveSize(mPivotXType,mPivotXValue,width,parentWidth);
        mPivotY = resolveSize(mPivotYType,mPivotYValue,height,parentHeight);
    }
}

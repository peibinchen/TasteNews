/*
        * Copyright (C) 2012 Capricorn
        *
        * Licensed under the Apache License, Version 2.0 (the "License");
        * you may not use this file except in compliance with the License.
        * You may obtain a copy of the License at
        *
        *      http://www.apache.org/licenses/LICENSE-2.0
        *
        * Unless required by applicable law or agreed to in writing, software
        * distributed under the License is distributed on an "AS IS" BASIS,
        * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
        * See the License for the specific language governing permissions and
        * limitations under the License.
*/


package com.example.asus.tastenews.ui.FloatingMenu;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.BaseInterpolator;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;
import android.view.animation.OvershootInterpolator;
import android.view.animation.RotateAnimation;

import com.example.asus.tastenews.R;

/**
 * Created by ASUS on 2016/7/20.
 * ArcLayout的作用是测量view的大小，对view进行布局已经根据是否要动画渲染来为每个view添加动画
 */
public class ArcLayout extends ViewGroup {
    public static final float DEFAULT_FROM_DEGREES = 270.0f;
    public static final float DEFAULT_TO_DEGREES = 360.0f;
    public static final int MIN_RADIUS = 100;

    private float mFromDegrees = DEFAULT_FROM_DEGREES;
    private float mToDegrees = DEFAULT_TO_DEGREES;
    private int mRadius = MIN_RADIUS;
    private int mChildPadding = 5;
    private int mLayoutPadding = 10;
    private boolean mExpanded = false;//用于指明menu是否已经处于展开状态

    private int mChildSize;

    public ArcLayout(Context context){
        super(context);
    }

    public ArcLayout(Context context, AttributeSet attrs){
        super(context,attrs);

        /*
        从layout布局中获取各种参数
         */
        if(attrs != null){
            TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.ArcLayout,0,0);
            mFromDegrees = a.getFloat(R.styleable.ArcLayout_fromDegrees,DEFAULT_FROM_DEGREES);
            mToDegrees = a.getFloat(R.styleable.ArcLayout_toDegrees,DEFAULT_TO_DEGREES);
            mChildSize = Math.max(a.getDimensionPixelSize(R.styleable.ArcLayout_childSize,0),0);

            a.recycle();
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec,int heightMeasureSpec){
        final int radius = mRadius
                = computeRadius(Math.abs(mFromDegrees - mToDegrees),getChildCount(),mChildSize,mChildPadding,MIN_RADIUS);
        //size的计算方法不懂，为什么mChildSize和吗ChildPadding不用*2，留个flag
        final int size = 2 * radius + mChildSize + mChildPadding + 2 * mLayoutPadding;
        //final int size = radius + mChildSize + mChildPadding + 2 * mLayoutPadding;

        setMeasuredDimension(size,size);

        final int count = getChildCount();
        for(int i = 0;i < count;i++){
            getChildAt(i).measure(MeasureSpec.makeMeasureSpec(mChildSize,MeasureSpec.EXACTLY),
                    MeasureSpec.makeMeasureSpec(mChildSize,MeasureSpec.EXACTLY));
        }
    }

    /**
     * onLayout函数是用于放置childView的，因此在这里需要得到每个view放置的rect的坐标，
     * 并且放置
     * @param changed
     * @param l
     * @param t
     * @param r
     * @param b
     */
    @Override
    protected void onLayout(boolean changed,int l,int t,int r,int b){
        final int centerX = getWidth() / 2;
        final int centerY = getHeight() / 2;
        final int radius = mExpanded ? mRadius : 0;

        final int childCount = getChildCount();
        float degrees = mFromDegrees;
        final float perDegrees = Math.abs(mFromDegrees - mToDegrees) / (childCount - 1);

        for(int i = 0;i<childCount;i++){
            Rect rect = computeChildFrame(centerX,centerY,radius,degrees,mChildSize);
            getChildAt(i).layout(rect.left, rect.top,rect.right,rect.bottom);
            degrees += perDegrees;
        }
    }

    public boolean getExpanded(){
        return mExpanded;
    }

    public void setArc(float fromDegrees,float toDegrees){
        if(mToDegrees == toDegrees && mFromDegrees == fromDegrees){
            return;
        }

        mToDegrees = toDegrees;
        mFromDegrees = fromDegrees;

        requestLayout();//当view发生改变时就需要调用这个方法，这个方法会对整个view tree进行重绘
    }

    public void setChildSize(int size){
        if(mChildSize == size || size < 0){
            return;
        }

        mChildSize = size;
        requestLayout();
    }

    public int getChildSize(){
        return mChildSize;
    }

    public void switchState(final boolean showAnimation){
        if(showAnimation){
            final int childCount = getChildCount();
            for(int i=0;i<childCount;i++){
                bindAnimationToChild(getChildAt(i),i,300);
            }
        }

        mExpanded = !mExpanded;

        if(!showAnimation){
            requestLayout();
        }

        invalidate();//这个函数会调用每个子view的onDraw()函数
    }

    public void setDegrees(float fromDegrees,float toDegrees){
        if(mFromDegrees == fromDegrees && mToDegrees == toDegrees)
            return;

        mFromDegrees = fromDegrees;
        mToDegrees = toDegrees;

        requestLayout();
    }


    /*****************************************内部实现*****************************************/

    private void bindAnimationToChild(final View child,final int index,final int duration){
        final boolean expanded = mExpanded;
        final int centerX = getWidth() / 2;
        final int centerY = getHeight() / 2;
        final int radius = expanded ? mRadius : 0;

        final int childCount = getChildCount();
        final float perDegrees = (mToDegrees - mFromDegrees) / (childCount - 1);
        Rect frame = computeChildFrame(centerX,centerY,radius,mFromDegrees + perDegrees * index,mChildSize);

        //这里不理解两个相减是什么意思，立flag
        final int toXDelta = frame.left - child.getLeft();
        final int toYDelta= frame.top - child.getTop();

        //插值器，根据是收回还是弹出决定采用哪种动画插值:
        //AccelerateInterpolator:一直加速直到目的
        //OvershootInterpolator：一直加速然后超出一段距离再弹回
        BaseInterpolator interpolator = mExpanded ? new AccelerateInterpolator() : new OvershootInterpolator(3.5f);
        final long startOffset = computeStartOffset(childCount,expanded,index,0.1f,duration,interpolator);

        Animation animation = expanded ? createShrinkAnimation(0,toXDelta,0,toYDelta,startOffset,duration,interpolator)
                : createExpandAnimation(0,toXDelta,0,toYDelta,startOffset,duration,interpolator);

        final boolean isLast = getTransformedIndex(expanded,childCount,index) == childCount - 1;
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                if(isLast){
                    //这里为什么要用postDelayed而不直接调用onAllAnimationsEnd（）函数，立flag
                    postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            onAllAnimationsEnd();
                        }
                    },0);
                }
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        child.setAnimation(animation);
    }

    private void onAllAnimationsEnd(){
        final int childCount = getChildCount();
        for(int i=0;i<childCount;i++){
            getChildAt(i).clearAnimation();
        }
        requestLayout();
    }

    private static Rect computeChildFrame(final int centerX,final int centerY,final int radius,final float degrees,
                                          final int childSize){
        final double childCenterX = centerX + radius * Math.cos(Math.toRadians(degrees));
        final double childCenterY = centerY + radius * Math.sin(Math.toRadians(degrees));

        return new Rect((int)(childCenterX - childSize / 2),(int)(childCenterY - childSize / 2),
                (int)(childCenterX + childSize / 2),(int)(childCenterY + childSize / 2));
    }

    private static Animation createShrinkAnimation(float fromXDelta, float toXDelta, float fromYDelta, float toYDelta,
                                                   long startOffset, long duration, Interpolator interpolator){
        AnimationSet animationSet = new AnimationSet(false);//false表示将使用自己的interpolator,true则表示使用Animation自带的interpolator
        animationSet.setFillAfter(true);//使得移动动画结束后view淡出消失，如果设置为false，则view会回到原来的位置再淡出

        //flag:为什么要除以2
        final long preDuration = duration / 2;
        //沿着中心旋转
        Animation rotateAnimation = new RotateAnimation(0,360,Animation.RELATIVE_TO_SELF,0.5f,Animation.RELATIVE_TO_SELF,0.5f);
        rotateAnimation.setStartOffset(startOffset);
        rotateAnimation.setDuration(preDuration);
        rotateAnimation.setInterpolator(new LinearInterpolator());
        rotateAnimation.setFillAfter(true);

        animationSet.addAnimation(rotateAnimation);

        Animation rotateAndTranslateAnimation = new RotateAndTranslateAnimation(fromXDelta,toXDelta,fromYDelta,toYDelta,360,720);
        rotateAndTranslateAnimation.setFillAfter(true);
        rotateAndTranslateAnimation.setInterpolator(interpolator);
        rotateAndTranslateAnimation.setDuration(duration - preDuration);
        rotateAndTranslateAnimation.setStartOffset(startOffset + preDuration);

        animationSet.addAnimation(rotateAndTranslateAnimation);

        return animationSet;
    }

    private static int computeRadius(final float arcDegrees,final int childCount,
                                     final int childSize,final int childPadding,final int minRadius){
        if(childCount < 0){
            return minRadius;
        }

        final float perDegrees = arcDegrees / (childCount - 1);//将角度根据childView的个数等分
        final float halfPerDegrees = perDegrees / 2;
        final int perSize = childSize + childPadding;
        final int halfPerSize = perSize / 2;
        //根据sin函数算出散开的半径大小，再与minRadius比较，返回较大值
        return Math.max((int)(halfPerSize / Math.sin(Math.toRadians(halfPerDegrees))),minRadius);
    }

    private static Animation createExpandAnimation(float fromXDelta, float toXDelta, float fromYDelta, float toYDelta,
                                                   long startOffset, long duration, Interpolator interpolator){
        Animation animation = new RotateAndTranslateAnimation(fromXDelta,toXDelta,fromYDelta,toYDelta,0,720);
        animation.setStartOffset(startOffset);
        animation.setDuration(duration);
        animation.setInterpolator(interpolator);
        animation.setFillAfter(true);

        return animation;
    }

    private static long computeStartOffset(final int childCount,final boolean expanded,final int index,
                                           final float delayPercent,final long duration,Interpolator interpolator){
        final float delay = delayPercent * duration;//计算出用于延迟的时间，也就是每个view要等多长才能弹出去
        final long viewDelay = (long)(getTransformedIndex(expanded,childCount,index) * delay);//计算出具体的view弹出去需要的等待时间
        final float totalDelay = delay * childCount;

        /*
        这里暂时不理解
        （1）为什么totalDelay=delay*childCount而不是delay*（childCount - 1)，毕竟第一个view是没有延迟的
        （2）normalizedDelay到底指的是什么？
         */
        float normalizedDelay = viewDelay / totalDelay;
        normalizedDelay = interpolator.getInterpolation(normalizedDelay);

        return (long)(normalizedDelay * totalDelay);
    }

    /**
     * 菜单弹出的时候是顺序弹出，收回的时候是反向收回
     * @param expanded
     * @param count
     * @param index
     * @return
     */
    private static int getTransformedIndex(final boolean expanded,int count,int index){
        if(expanded){
            return count - 1 - index;
        }else
            return index;
    }

}

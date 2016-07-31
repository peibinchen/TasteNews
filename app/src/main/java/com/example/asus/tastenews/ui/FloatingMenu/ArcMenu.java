package com.example.asus.tastenews.ui.FloatingMenu;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.AnticipateOvershootInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.RotateAnimation;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.example.asus.tastenews.R;
import com.example.asus.tastenews.floatingwindow.widget.FloatWindow;
import com.example.asus.tastenews.utils.LogUtils;

/**
 * Created by ASUS on 2016/7/21.
 */
public class ArcMenu extends RelativeLayout {
    private ArcLayout mArcLayout;
    private ImageView mHintView;

    private FloatWindow mFloatWindow;
    private final int MAX_CLICK_TIME = 500;
    private long mLastTouchTime = 0;

    public ArcMenu(Context context){
        super(context);
        init(context);
    }
    public ArcMenu(Context context,AttributeSet attrs){
        super(context,attrs);
        init(context);
        applyAttrs(attrs);
    }

    public void addView(View view,OnClickListener listener){
        mArcLayout.addView(view);
        view.setOnClickListener(getItemClickListener(listener));
    }

    public void setViewDegrees(float fromDegrees,float toDegrees){
        mArcLayout.setDegrees(fromDegrees,toDegrees);
    }

    public void setAttatchedFloatWindow(FloatWindow floatWindow){
        mFloatWindow = floatWindow;
    }

    /*********************************************内部实现*****************************************************/

    private void init(Context context){
        LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.ui_arc_menu,this);

        mArcLayout  = (ArcLayout)findViewById(R.id.item_layout);
        mHintView = (ImageView)findViewById(R.id.control_image);

        final ViewGroup controlLayout = (ViewGroup)findViewById(R.id.control_layout);
        controlLayout.setClickable(true);

        controlLayout.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                LogUtils.d("BACKTEST","test is true");
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    mLastTouchTime = System.currentTimeMillis();
                    LogUtils.d("TRACETIME","lastTime is " + mLastTouchTime);
                    LogUtils.d("TRACEDOWN","event x = " + event.getRawX() + "   event y = " + event.getRawY());
                    /*
                    if (mFloatWindow != null)
                        mFloatWindow.down(event);
                        */
                }
                if (event.getAction() == MotionEvent.ACTION_MOVE) {
                    /*
                    if (mFloatWindow != null)
                        mFloatWindow.move(event);
                        */
                }
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    long currentTime = System.currentTimeMillis();
                    LogUtils.d("TRACETIME","currentTime is " + currentTime);
                    if(currentTime - mLastTouchTime < MAX_CLICK_TIME){
                        LogUtils.d("TRACEBYCPB", "ArcMenu layout onTouchListener is called");

                        mHintView.startAnimation(createHintSwitchAnimation(mArcLayout.getExpanded()));
                        mArcLayout.switchState(true);//将layout下的所有子view设置动画并且布局
                        //mFloatWindow.setIsJustMoveInUpMotion(false);
                    }else{
                        //mArcLayout.switchState(false);
                        //mFloatWindow.setIsJustMoveInUpMotion(true);
                    }

                    /*
                    if (mFloatWindow != null)
                        mFloatWindow.up(event);
                        */
                }
                if(event.getAction() == MotionEvent.ACTION_OUTSIDE) {
                    /*
                    if (mFloatWindow != null)
                        mFloatWindow.turnoffMenu();
                        */
                }
                return false;//将事件继续传递下去
            }
        });
        /*
        controlLayout.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                mHintView.startAnimation(createHintSwitchAnimation(mArcLayout.getExpanded()));
                mArcLayout.switchState(true);//将layout下的所有子view设置动画并且布局
            }
        });
        */
    }

    private void applyAttrs(AttributeSet attrs){
        if(attrs != null){
            TypedArray a = getContext().obtainStyledAttributes(attrs,R.styleable.ArcLayout,0,0);

            float fromDegrees = a.getFloat(R.styleable.ArcLayout_fromDegrees,ArcLayout.DEFAULT_FROM_DEGREES);
            float toDegrees = a.getFloat(R.styleable.ArcLayout_toDegrees,ArcLayout.DEFAULT_TO_DEGREES);
            mArcLayout.setArc(fromDegrees,toDegrees);

            int childSize = a.getDimensionPixelSize(R.styleable.ArcLayout_childSize,mArcLayout.getChildSize());
            mArcLayout.setChildSize(childSize);

            a.recycle();
        }
    }

    /**
     * 为每个ArcLayout下的view绑定动画，并且在动画结束后清除所有动画，并且重绘
     * 与此同时将点击事件传递到listener
     * @param listener
     * @return
     */
    private OnClickListener getItemClickListener(final OnClickListener listener){
        return new OnClickListener() {
            @Override
            public void onClick(View v) {
                Animation animation = bindItemAnimation(v,true,400);
                //在动画结束后清除所有的view的动画，并且重绘
                animation.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                itemDisappear();
                            }
                        },0);
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {

                    }
                });

                final int childCount = mArcLayout.getChildCount();
                for(int i=0;i<childCount;i++){
                    View unClickView = mArcLayout.getChildAt(i);
                    if(unClickView != v)
                        bindItemAnimation(mArcLayout.getChildAt(i),false,300);
                }

                mArcLayout.invalidate();
                mHintView.startAnimation(createHintSwitchAnimation(true));

                if(listener != null){
                    listener.onClick(v);
                }
            }
        };
    }

    private void itemDisappear(){
        final int childCount = mArcLayout.getChildCount();
        for(int i = 0;i < childCount;i++){
            mArcLayout.getChildAt(i).clearAnimation();
        }
        mArcLayout.switchState(false);
    }

    private Animation bindItemAnimation(final View childView,final boolean isClicked,final long duration){
        Animation animation = createItemDisappearAnimation(duration,isClicked);
        childView.setAnimation(animation);
        return animation;
    }

    /**
     * 创建消失动画，当某个子item被点击时，被点击的item会变大一倍，其余的item会缩小到不见，
     * 与此同时所有的item都会逐渐变透明，最终消失
     * @param duration
     * @param isClicked
     * @return
     */
    private static Animation createItemDisappearAnimation(final long duration,final boolean isClicked){
        AnimationSet animationSet = new AnimationSet(true);
        animationSet.addAnimation(new ScaleAnimation(1.0f,isClicked ? 2.0f : 0.0f,1.0f,isClicked ? 2.0f : 0.0f,
                Animation.RELATIVE_TO_SELF,0.5f,Animation.RELATIVE_TO_SELF,0.5f));
        animationSet.addAnimation(new AlphaAnimation(1.0f,0.0f));//从完全不透明变成完全透明

        animationSet.setFillAfter(true);
        animationSet.setDuration(duration);
        animationSet.setInterpolator(new DecelerateInterpolator());

        return animationSet;
    }

    /**
     * 实现imageView在点击后旋转45度的动画效果
     * @param expanded
     * @return
     */
    private static Animation createHintSwitchAnimation(final boolean expanded){
        LogUtils.d("EXPANED","expanded is " + expanded);
        Animation animation = new RotateAnimation(expanded ? 45 : 0,expanded ? 0 : 45,
                Animation.RELATIVE_TO_SELF,0.5f,Animation.RELATIVE_TO_SELF,0.5f);
        animation.setFillAfter(true);
        animation.setInterpolator(new AnticipateOvershootInterpolator());
        animation.setDuration(100);
        animation.setStartOffset(0);

        return animation;
    }
}

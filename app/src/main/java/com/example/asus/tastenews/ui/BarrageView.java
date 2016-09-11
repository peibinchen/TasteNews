package com.example.asus.tastenews.ui;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.Random;

/**
 * Created by ASUS on 2016/7/17.
 * 说明：这个是弹幕控件，使用线程重绘技术来实现view的移动，这里其实还可以用动画实现
 * 立flag：自己实现一遍用动画实现的弹幕控件，并且与线程重绘比较。
 */
public class BarrageView extends TextView {
    private int mPosX;
    private int mPosY;
    private int mWindowWidth;
    private int mWindowHeight;
    private int mColor = 0xffffffff;
    private int mSize = 30;

    private String mText = null;
    private Random random = new Random();
    private Paint mPaint = new Paint();

    private final int MAX_TEXT_SIZE = 60;
    private final int MIN_TEXT_SIZE = 30;

    private RollThread mThread;

    public BarrageView(Context context){
        super(context);
        init();
    }
    public BarrageView(Context context,AttributeSet attr){
        super(context,attr);
        init();
    }
    public BarrageView(Context context,AttributeSet attr,int defStyle){
        super(context,attr,defStyle);
        init();
    }

    public void setText(String text){
        mText = text;
    }
    public String getText(){
        return mText;
    }

    private void init(){
        mColor = Color.rgb(random.nextInt(256),random.nextInt(256),random.nextInt(256));
        mSize = MIN_TEXT_SIZE + random.nextInt(MAX_TEXT_SIZE - MIN_TEXT_SIZE);
        mPaint.setColor(mColor);
        mPaint.setTextSize(mSize);

        Rect rect = new Rect();
        getWindowVisibleDisplayFrame(rect);//测量窗口大小，通过rect返回
        mWindowHeight = rect.height();
        mWindowWidth = rect.width();
        mPosX = mWindowWidth;
        mPosY = mSize + random.nextInt(mWindowHeight - mSize);
    }

    @Override
    protected void onDraw(Canvas canvas){
        canvas.drawText(getText(),mPosX,mPosY,mPaint);
        if(mThread == null){
            mThread = new RollThread();
            mThread.start();
        }
    }

    @Override
    protected void onWindowVisibilityChanged(int visibility){
        super.onWindowVisibilityChanged(visibility);
        if(mThread == null){
            return;
        }
        if(View.GONE == visibility){
            mThread.onPause();
        }else if(View.VISIBLE == visibility){
            mThread.onResume();
        }
    }

    class RollThread extends Thread{
        private final Object mPauseLock;
        private boolean mPauseFlag;

        public RollThread(){
            mPauseLock = new Object();
            mPauseFlag = false;
        }

        @Override
        public void run(){
            while(true){
                checkPause();
                moveX();
                postInvalidate();//调用onDraw()函数实现重绘
                try{
                    Thread.sleep(30);
                }catch(Exception e){
                    e.printStackTrace();
                }
                if(breakRunThread()){
                    //特别注意修改UI只能在主线程，快速回到主线程可以用post,runOnUiThread或者handler
                    post(new Runnable() {
                        @Override
                        public void run() {
                            ((ViewGroup)(BarrageView.this.getParent())).removeView(BarrageView.this);
                        }
                    });
                    break;
                }
            }
        }

        public void onPause(){
            synchronized (mPauseLock){
                mPauseFlag = true;
            }
        }

        public void onResume(){
            synchronized (mPauseLock){
                mPauseFlag = false;
                mPauseLock.notify();
            }
        }

        private void checkPause(){
            synchronized (mPauseLock){
                if(mPauseFlag){
                    try{
                        mPauseLock.wait();
                    }catch(InterruptedException e){
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    private boolean breakRunThread(){
        if(mPosX <= -mPaint.measureText(getText())){
            return true;
        }else
            return false;
    }

    private void moveX(){
        mPosX -= 10;
    }
}

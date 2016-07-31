package com.example.asus.tastenews.floatingwindow.widget;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.text.method.LinkMovementMethod;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.asus.tastenews.R;
import com.example.asus.tastenews.application.NewsApplication;
import com.example.asus.tastenews.beans.NewsBean;
import com.example.asus.tastenews.floatingwindow.model.FloatingWindowModelImpl;
import com.example.asus.tastenews.floatingwindow.presenter.FloatingWindowPresenter;
import com.example.asus.tastenews.floatingwindow.presenter.FloatingWindowPresenterImpl;
import com.example.asus.tastenews.floatingwindow.view.FloatingWindowView;
import com.example.asus.tastenews.floatingwindow.utils.PlayerState;
import com.example.asus.tastenews.news.view.NewsDetailView;
import com.example.asus.tastenews.news.widget.NewsDetailActivity;
import com.example.asus.tastenews.news.widget.NewsFragment;
import com.example.asus.tastenews.ui.FloatingMenu.ArcMenu;
import com.example.asus.tastenews.utils.LogUtils;

import org.sufficientlysecure.htmltextview.HtmlTextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ASUS on 2016/7/1.
 */
public class FloatingNewsService extends Service
        implements FloatingWindowView,View.OnClickListener{

    //floatWindow是管理类
    private FloatWindow mFloatWindow;

    private View mFloatView,mPlayerView;

    //playerView的控件
    private TextView mTitleTV;
    private HtmlTextView mContentHTV;
    private TextView mJumpToNewsLinkTV;
    private ImageButton mPrevIB;
    private ImageButton mPauseOrStartIB;
    private ImageButton mNextIB;

    //floatView的控件
    private ImageView mLogoIV;
    private ImageView mHandleIV;
    //private ArcMenu mArcMenu;

    private String mTitleText;
    private NewsBean mCurrentNewsBean;

    private List<NewsBean> mNewsList = new ArrayList<>();
    private int mType = NewsFragment.NEWS_TYPE_TOP;

    private ProgressBar mProgressBar;
    private FloatingWindowPresenter mPresenter;

    private int mIndex = 0;
    private int mPage = 0;

    public class FloatingNewsBinder extends Binder {
        public FloatingNewsService getService(){
            return FloatingNewsService.this;
        }
    }

    @Override
    public IBinder onBind(Intent intent){
        return new FloatingNewsBinder();
    }

    @Override
    public void onCreate(){
        super.onCreate();
        initFloatWindow();

        mPresenter = new FloatingWindowPresenterImpl(this,getApplicationContext());
        mPresenter.loadNewsInFloatingWindow(mType,mPage);
    }

    public void show(){
        if(null != mFloatWindow){
            mFloatWindow.show();
        }
    }


    @Override
    public void getNews(List<NewsBean> newsList) {
        LogUtils.d("TAGGGO","getNews() " + newsList.size());
        mNewsList = newsList;
        showOneNews();
    }

    @Override
    public void showContentInView(String content) {
        if(mContentHTV == null)
            return;
        mContentHTV.setHtmlFromString(content,new HtmlTextView.RemoteImageGetter());
        mTitleTV.setText(mTitleText);
    }

    @Override
    public void showErrorMessage(String message) {
        LogUtils.d("TAGGG","showErrorMessage(message)");
        Toast.makeText(getApplicationContext(),message,Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showProgressbar() {
        mProgressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideProgressbar() {
        mProgressBar.setVisibility(View.GONE);
    }


    @Override
    public void start() {
        showOneNews();
    }

    @Override
    public void pause() {
    }

    @Override
    public void next() {
        showOneNews();
    }

    @Override
    public void previous() {
        mIndex -= 2;//注意这里是自减2，自减1是停留在原地
        showOneNews();
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.float_ib_next:
                next();
                break;
            case R.id.float_ib_previous:
                previous();
                break;
            case R.id.jumptonews:
                jumpToNews();
                break;
            default:
                start();
                break;
        }
    }

    /************************************************内部实现*********************************************/

    private void showOneNews(){
        if (mIndex >= 0 && mIndex < mNewsList.size()) {
            mCurrentNewsBean = mNewsList.get(mIndex);
            mPresenter.loadNewsDetailInFloatingWindow(mCurrentNewsBean.getDocid());
            mTitleText = mNewsList.get(mIndex).getTitle();
            mIndex++;
        }else if(mIndex < 0){
            mIndex = 1;
        }else{
            LogUtils.d("TAGGGO","index is " + mIndex + "   mPage is " + mPage);
            mPage = 0;
            mIndex = 0;
            if(mNewsList != null)
                mNewsList.clear();
            mPresenter.loadNewsInFloatingWindow(mType,mPage);
        }
    }

    private void jumpToNews(){
        Context context = NewsApplication.getNewsContext();
        Intent intent = new Intent(NewsApplication.getNewsContext(), NewsDetailActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra("news",mCurrentNewsBean);
        context.startActivity(intent);
        mFloatWindow.turnoffMenu();
    }

    /**
     * 用于初始化floatView和playerView中的控件，设置点击事件，
     * 同时初始化floatWindow，将floatView和playerView绑定到floatWindow中，
     * 便于管理。
     */
    private void initFloatWindow(){
        mFloatView = LayoutInflater.from(getApplicationContext()).inflate(R.layout.item_floatnews_float,null);
        mPlayerView = LayoutInflater.from(getApplicationContext()).inflate(R.layout.item_floatnews_menu,null);

        mContentHTV = (HtmlTextView)mPlayerView.findViewById(R.id.float_htv_content);
        mTitleTV = (TextView)mPlayerView.findViewById(R.id.float_tv_title);
        mNextIB = (ImageButton)mPlayerView.findViewById(R.id.float_ib_next);
        mPrevIB = (ImageButton)mPlayerView.findViewById(R.id.float_ib_previous);
        mPauseOrStartIB = (ImageButton)mPlayerView.findViewById(R.id.float_ib_play);
        mJumpToNewsLinkTV = (TextView)mPlayerView.findViewById(R.id.jumptonews);

        //playerView设置监听事件
        mNextIB.setOnClickListener(this);
        mPrevIB.setOnClickListener(this);
        mPauseOrStartIB.setOnClickListener(this);
        mJumpToNewsLinkTV.setOnClickListener(this);
        mJumpToNewsLinkTV.setMovementMethod(LinkMovementMethod.getInstance());

        //floatView中的mHandleIV和mLogoIV的src都使用drawable中的文件操控，使得他们能旋转
        //mHandleIV = (ImageView)mFloatView.findViewById(R.id.float_iv_handle);
        // mLogoIV = (ImageView)mFloatView.findViewById(R.id.float_iv_logo);
        //mArcMenu = (ArcMenu)mFloatView.findViewById(R.id.floating_menu);

        //FloatWindow是playerView和floatView的管理类，用于对playerView和floatView的点击事件进行处理
        mFloatWindow = new FloatWindow(this);
        mFloatWindow.setFloatView(mFloatView);
        mFloatWindow.setPlayerView(mPlayerView);
        //mFloatWindow.setArcMenu(mArcMenu);//在up的时候触发动画
        //mArcMenu.setAttatchedFloatWindow(mFloatWindow);

        mProgressBar = new ProgressBar(getApplicationContext());
    }
}

package com.example.asus.tastenews.floatingwindow.widget;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.asus.tastenews.R;
import com.example.asus.tastenews.application.NewsApplication;
import com.example.asus.tastenews.beans.NewsBean;
import com.example.asus.tastenews.floatingwindow.presenter.FloatingWindowPresenter;
import com.example.asus.tastenews.floatingwindow.presenter.FloatingWindowPresenterImpl;
import com.example.asus.tastenews.floatingwindow.view.FloatingWindowView;
import com.example.asus.tastenews.floatingwindow.NewsTagsAdapter;
import com.example.asus.tastenews.news.widget.NewsDetailActivity;
import com.example.asus.tastenews.news.widget.NewsFragment;
import com.example.asus.tastenews.ui.TagCloud.TagCloudView;
import com.example.asus.tastenews.ui.TagCloud.TagsAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by SomeOneInTheWorld on 2016/7/1.
 */
public class NewsTagService extends Service
        implements FloatingWindowView,NewsTagsAdapter.OnFloatWindowClose{

    //floatWindow是管理类
    private FloatWindow mFloatWindow;

    private View mFloatView,mPlayerView;

    private TagCloudView fragmentTagcloud;

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

    private TagsAdapter mAdapter;

    public class FloatingNewsBinder extends Binder {
        public NewsTagService getService(){
            return NewsTagService.this;
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
        mNewsList = newsList;
        if(mAdapter != null && mAdapter instanceof NewsTagsAdapter){
            ((NewsTagsAdapter)mAdapter).setDataSource(mNewsList);
        }
        mFloatWindow.setTagsAdapter(mAdapter);
    }

    @Override
    public void showContentInView(String content) {
    }

    @Override
    public void showErrorMessage(String message) {
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

    /************************************************内部实现*********************************************/


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
        mPlayerView = LayoutInflater.from(getApplicationContext()).inflate(R.layout.fragment_tagcloud,null);

        fragmentTagcloud = (TagCloudView) mPlayerView.findViewById(R.id.fragment_tagcloud);
        mAdapter = new NewsTagsAdapter(this,new String[20]);
        mFloatWindow = new FloatWindow(this);
        mFloatWindow.setFloatView(mFloatView);
        mFloatWindow.setPlayerView(mPlayerView);
        mFloatWindow.setTagCloud(fragmentTagcloud);
        mFloatWindow.setTagsAdapter(mAdapter);

        mProgressBar = new ProgressBar(getApplicationContext());
    }

    @Override
    public void jumpToNews(NewsBean item) {
        mCurrentNewsBean = item;
        jumpToNews();
    }
}

package com.example.asus.tastenews.floatingwindow.presenter;

import android.content.Context;

import com.example.asus.tastenews.beans.NewsBean;
import com.example.asus.tastenews.beans.NewsDetailBean;
import com.example.asus.tastenews.floatingwindow.model.FloatingWindowModel;
import com.example.asus.tastenews.floatingwindow.model.FloatingWindowModelImpl;
import com.example.asus.tastenews.floatingwindow.view.FloatingWindowView;

import java.util.List;


/**
 * Created by SomeOneInTheWorld on 2016/7/18.
 */
public class FloatingWindowPresenterImpl
        implements FloatingWindowPresenter,FloatingWindowModelImpl.OnLoadNewsListListener,FloatingWindowModelImpl.OnLoadNewsDetailListener {
    private FloatingWindowModel mModel;
    private FloatingWindowView mView;
    private Context mContext;

    public FloatingWindowPresenterImpl(FloatingWindowView view,Context context){
        mView = view;
        mModel = new FloatingWindowModelImpl();
        mContext = context;
    }

    @Override
    public void loadNewsInFloatingWindow(int type,int page) {
        mView.showProgressbar();
        mModel.loadNews(type,page,this);
    }

    @Override
    public void onSuccess(NewsDetailBean newsDetailBean) {
        mView.hideProgressbar();
        mView.showContentInView(newsDetailBean.getBody());
    }

    @Override
    public void onFailureDetail(String msg, Exception e) {
        mView.hideProgressbar();
        mView.showErrorMessage(msg);
    }

    @Override
    public void loadNewsDetailInFloatingWindow(String docid) {
        mView.showProgressbar();
        mModel.loadNewsDetail(docid,this);
    }

    @Override
    public void onSuccess(List<NewsBean> list) {
        mView.hideProgressbar();
        mView.getNews(list);
    }

    @Override
    public void onFailure(String msg, Exception e) {
        mView.hideProgressbar();
        mView.showErrorMessage(msg);
    }
}

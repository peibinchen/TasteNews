package com.example.asus.tastenews.news.presenter;

import android.content.Context;
import android.support.design.widget.Snackbar;
import android.widget.Toast;

import com.example.asus.tastenews.beans.CommentBean;
import com.example.asus.tastenews.beans.NewsDetailBean;
import com.example.asus.tastenews.beans.UserBean;
import com.example.asus.tastenews.news.model.NewsModel;
import com.example.asus.tastenews.news.model.NewsModelImpl;
import com.example.asus.tastenews.news.view.NewsDetailView;

import java.util.List;

/**
 * Created by ASUS on 2016/5/28.
 */
public class NewsDetailPresenterImpl implements NewsDetailPresenter,NewsModelImpl.OnLoadNewsDetailListener,NewsModelImpl.OnCommentDetailNewsListener{

    private Context mContext;
    private NewsDetailView mNewsDetailView;
    private NewsModel mNewsModel;

    public NewsDetailPresenterImpl(Context context,NewsDetailView newsDetailView){
        mContext = context;
        mNewsDetailView = newsDetailView;
        mNewsModel = new NewsModelImpl();
    }

    @Override
    public void loadNewsDetail(String docid){
        mNewsDetailView.showProgress();
        mNewsModel.loadNewsDetail(docid,this);
    }

    @Override
    public void sendCommentToNews(UserBean user, String comment){
        mNewsDetailView.showProgress();
        mNewsModel.sendCommentInDetailNews(user,comment,this);
    }

    @Override
    public void onSuccess(NewsDetailBean detail, List<CommentBean> commentBeanList){
        if(detail != null){
            mNewsDetailView.showNewsDetailContent(detail.getBody(),commentBeanList);
        }
        mNewsDetailView.hideProgress();
    }

    @Override
    public void onFailure(String msg,Exception e){
        mNewsDetailView.hideProgress();
    }


    @Override
    public void onSuccess(String comment) {
        Toast.makeText(mContext,"评论成功",Toast.LENGTH_SHORT).show();
        mNewsDetailView.hideProgress();
        mNewsDetailView.successComment(comment);
    }

    @Override
    public void onFailure(int i, String s) {
        Toast.makeText(mContext,"评论失败" + s,Toast.LENGTH_SHORT).show();
        mNewsDetailView.hideProgress();
        mNewsDetailView.failureComment();
    }
}

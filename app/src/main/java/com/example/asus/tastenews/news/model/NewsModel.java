package com.example.asus.tastenews.news.model;

import com.example.asus.tastenews.beans.UserBean;

/**
 * Created by ASUS on 2016/5/24.
 */
public interface NewsModel {

    void loadNews(String url,int type,NewsModelImpl.OnLoadNewsListListener listener);
    void loadNewsDetail(String docid,NewsModelImpl.OnLoadNewsDetailListener listener);
    void sendCommentInDetailNews(UserBean user, String comment, NewsModelImpl.OnCommentDetailNewsListener listener);
}

package com.example.asus.tastenews.news.presenter;

import com.example.asus.tastenews.beans.UserBean;

/**
 * Created by SomeOneInTheWorld on 2016/5/28.
 */
public interface NewsDetailPresenter {

    void loadNewsDetail(String docId);
    void sendCommentToNews(UserBean user,String comment);
}

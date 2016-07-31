package com.example.asus.tastenews.floatingwindow.view;

import com.example.asus.tastenews.beans.NewsBean;

import java.util.List;

/**
 * Created by ASUS on 2016/7/1.
 */
public interface FloatingWindowView {
    void start();
    void pause();
    void next();
    void previous();
    void getNews(List<NewsBean> newsList);
    void showErrorMessage(String message);
    void showProgressbar();
    void hideProgressbar();
    void showContentInView(String content);
}

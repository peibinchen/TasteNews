package com.example.asus.tastenews.floatingwindow.view;

import com.example.asus.tastenews.beans.NewsBean;

import java.util.List;

/**
 * Created by SomeOneInTheWorld on 2016/7/1.
 */
public interface FloatingWindowView {
    void getNews(List<NewsBean> newsList);
    void showErrorMessage(String message);
    void showProgressbar();
    void hideProgressbar();
    void showContentInView(String content);
}

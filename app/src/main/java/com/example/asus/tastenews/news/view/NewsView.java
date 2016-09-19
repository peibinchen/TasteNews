package com.example.asus.tastenews.news.view;

import com.example.asus.tastenews.beans.NewsBean;

import java.util.List;

/**
 * Created by SomeOneInTheWorld on 2016/5/25.
 */
public interface NewsView {
    void showProgress();
    void addNews(List<NewsBean>newsBean);
    void hideProgress();
    void showLoadFailMsg();
}

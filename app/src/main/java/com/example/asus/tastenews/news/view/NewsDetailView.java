package com.example.asus.tastenews.news.view;

import com.example.asus.tastenews.beans.CommentBean;

import java.util.List;

/**
 * Created by ASUS on 2016/5/25.
 */
public interface NewsDetailView {
    void showNewsDetailContent(String newsDetailContent, List<CommentBean> commentBeanList);
    void showProgress();
    void hideProgress();
    void successComment(String comment);
    void failureComment();
}

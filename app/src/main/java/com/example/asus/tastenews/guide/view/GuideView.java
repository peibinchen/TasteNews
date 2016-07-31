package com.example.asus.tastenews.guide.view;


/**
 * Created by ASUS on 2016/6/3.
 */
public interface GuideView {
    void showProgressbar();
    void hideProgressbar();
    void registerNewUser();
    void onSuccessFound();
    void onFailureFound(String message);
}

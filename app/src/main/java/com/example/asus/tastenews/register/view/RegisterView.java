package com.example.asus.tastenews.register.view;

/**
 * Created by ASUS on 2016/6/4.
 */
public interface RegisterView {
    void showProgress();
    void hideProgress();
    void showSuccessRegister();
    void showFailureRegister(String error);
}

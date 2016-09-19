package com.example.asus.tastenews.register.presenter;

import android.content.Context;

import com.example.asus.tastenews.beans.UserBean;
import com.example.asus.tastenews.register.model.RegisterModel;
import com.example.asus.tastenews.register.model.RegisterModelImpl;
import com.example.asus.tastenews.register.view.RegisterView;

/**
 * Created by SomeOneInTheWorld on 2016/6/4.
 */
public class RegisterPresenterImpl implements RegisterPresenter,RegisterModelImpl.OnRegisterListener {

    private RegisterView mView;
    private RegisterModel mModel;
    private Context mContext;

    public RegisterPresenterImpl(RegisterView view,Context context){
        mView = view;
        mModel = new RegisterModelImpl();
        mContext = context;
    }

    @Override
    public void registerNewUser(UserBean user){
        mView.showProgress();
        mModel.register(user,mContext,this);
    }

    @Override
    public void onSuccess(){
        mView.hideProgress();
        mView.showSuccessRegister();
    }

    @Override
    public void onFailure(int i,String s){
        mView.hideProgress();
        mView.showFailureRegister(s);
    }
}

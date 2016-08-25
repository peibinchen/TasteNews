package com.example.asus.tastenews.guide.presenter;

import android.content.Context;

import com.example.asus.tastenews.beans.UserBean;
import com.example.asus.tastenews.guide.model.GuideModel;
import com.example.asus.tastenews.guide.model.GuideModelImpl;
import com.example.asus.tastenews.guide.view.GuideView;

/**
 * Created by ASUS on 2016/6/3.
 */
public class GuidePresenterImpl implements GuidePresenter,GuideModelImpl.OnCheckUserListener{
    private GuideView mView;
    private GuideModel mModel;
    private Context mContext;

    public GuidePresenterImpl(GuideView view,Context context){
        this.mView = view;
        mContext = context;
        mModel = new GuideModelImpl(mContext);
    }

    @Override
    public void loadUserIfExist(UserBean user){
        mView.showProgressbar();
        mModel.getUserFromCloud(user,this);
    }

    @Override
    public void onSuccess(){
        mView.hideProgressbar();
        mView.onSuccessFound();
    }

    @Override
    public void onFailure(String message){
        mView.hideProgressbar();
        mView.onFailureFound(message);
    }
}

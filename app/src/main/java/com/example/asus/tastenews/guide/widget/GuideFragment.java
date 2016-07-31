package com.example.asus.tastenews.guide.widget;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.asus.tastenews.R;
import com.example.asus.tastenews.beans.UserBean;
import com.example.asus.tastenews.guide.presenter.GuidePresenter;
import com.example.asus.tastenews.guide.presenter.GuidePresenterImpl;
import com.example.asus.tastenews.guide.view.GuideView;
import com.example.asus.tastenews.main.widget.MainActivity;
import com.example.asus.tastenews.news.widget.NewsFragment;
import com.example.asus.tastenews.register.widget.RegisterFragment;

import cn.bmob.v3.listener.SaveListener;
import mehdi.sakout.fancybuttons.FancyButton;

/**
 * Created by ASUS on 2016/6/3.
 */
public class GuideFragment extends Fragment implements GuideView {

    private GuidePresenter mPresenter;

    private EditText mUserNameET;
    private EditText mUserPasswordET;
    private FancyButton mLoginBT;
    private FancyButton mRegisterBT;
    private ProgressBar mProgressBar;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        mPresenter = new GuidePresenterImpl(this,getActivity().getApplicationContext());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        final View view = inflater.inflate(R.layout.fragment_guide,null);
        initViews(view);

        mLoginBT.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                UserBean user = new UserBean();
                String userName = mUserNameET.getText().toString();
                String password = mUserPasswordET.getText().toString();
                user.setUsername(userName);
                user.setPassword(password);
                mPresenter.loadUserIfExist(user);
            }
        });

        mRegisterBT.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                registerNewUser();
            }
        });

        return view;
    }

    private void initViews(View view){
        mUserNameET = (EditText)view.findViewById(R.id.et_username);
        mUserPasswordET = (EditText)view.findViewById(R.id.et_password);
        mLoginBT = (FancyButton)view.findViewById(R.id.bt_login);
        mRegisterBT = (FancyButton)view.findViewById(R.id.bt_register);
        mProgressBar = (ProgressBar)view.findViewById(R.id.progress_guide);
    }

    @Override
    public void showProgressbar() {
        mProgressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideProgressbar() {
        mProgressBar.setVisibility(View.GONE);
    }

    @Override
    public void registerNewUser() {
        getActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.frame_content,new RegisterFragment()).commit();
    }

    @Override
    public void onSuccessFound() {
        /*
        getActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.frame_content,new NewsFragment()).commit();
                */
        Intent view = new Intent(getActivity(), MainActivity.class);
        view.setAction(Intent.ACTION_VIEW);
        startActivity(view);
        getActivity().finish();
    }

    @Override
    public void onFailureFound(String message) {
        Toast.makeText(getActivity().getApplicationContext(),message,Toast.LENGTH_SHORT).show();
    }
}

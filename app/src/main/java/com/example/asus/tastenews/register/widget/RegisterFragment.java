package com.example.asus.tastenews.register.widget;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.example.asus.tastenews.R;
import com.example.asus.tastenews.beans.UserBean;
import com.example.asus.tastenews.main.widget.MainActivity;
import com.example.asus.tastenews.register.presenter.RegisterPresenter;
import com.example.asus.tastenews.register.presenter.RegisterPresenterImpl;
import com.example.asus.tastenews.register.view.RegisterView;

import mehdi.sakout.fancybuttons.FancyButton;

/**
 * Created by SomeOneInTheWorld on 2016/6/4.
 */

public class RegisterFragment extends Fragment implements RegisterView {

    private RegisterPresenter mPresenter;

    private EditText mRegisterName;
    private EditText mRegisterPassword;
    private FancyButton mRegisterButton;
    private ProgressBar mProgressBar;

    private View mView;

    @Override
    public void onCreate(Bundle savedInstance){
        super.onCreate(savedInstance);
        mPresenter = new RegisterPresenterImpl(this,getActivity().getApplicationContext());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup viewGroup,Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.fragment_register,viewGroup,false);

        mRegisterName = (EditText)view.findViewById(R.id.username_register);
        mRegisterPassword = (EditText)view.findViewById(R.id.password_register);
        mRegisterButton = (FancyButton)view.findViewById(R.id.bt_register);
        mProgressBar = (ProgressBar)view.findViewById(R.id.progress_register);

        mRegisterButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                UserBean user = new UserBean();
                user.setUsername(mRegisterName.getText().toString());
                user.setPassword(mRegisterPassword.getText().toString());
                mPresenter.registerNewUser(user);
            }
        });

        mView = view;
        return view;
    }


    @Override
    public void showProgress() {
        mProgressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideProgress() {
        mProgressBar.setVisibility(View.GONE);
    }

    @Override
    public void showSuccessRegister() {
        Snackbar.make(mView,getString(R.string.register_success),Snackbar.LENGTH_SHORT).show();
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
    public void showFailureRegister(String error) {
        Snackbar.make(mView,getString(R.string.register_failure) + "   " + error,Snackbar.LENGTH_SHORT).show();
    }
}

package com.example.asus.tastenews.news.widget;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.asus.tastenews.R;
import com.example.asus.tastenews.beans.NewsBean;
import com.example.asus.tastenews.beans.QuestionTable;
import com.example.asus.tastenews.beans.UserBean;
import com.example.asus.tastenews.news.presenter.CommentPresenter;
import com.example.asus.tastenews.utils.LogUtils;
import com.github.clans.fab.FloatingActionButton;
import com.rengwuxian.materialedittext.MaterialEditText;

/**
 * Created by ASUS on 2016/8/23.
 */
public class QuestionFragment extends BaseFragment {
    private MaterialEditText mMainQuestionET;
    private MaterialEditText mQuestionDescriptionET;
    private FloatingActionButton mSubmitBT;
    private ProgressBar mProgressBar;

    private NewsBean mNewsBean;
    private QuestionTable questionTable;
    private CommentPresenter mPresenter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_question,container,false);
        mPresenter = getPresenter();
        initLayout(view);
        super.onCreateView(inflater, container, savedInstanceState);
        return view;
    }

    private void initLayout(View view){
        mMainQuestionET = (MaterialEditText)view.findViewById(R.id.met_main_quesiton);
        mQuestionDescriptionET = (MaterialEditText)view.findViewById(R.id.met_question_description);
        mSubmitBT = (FloatingActionButton)view.findViewById(R.id.fab_submit_question);
        mProgressBar = (ProgressBar)view.findViewById(R.id.progress);

        mSubmitBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mNewsBean = getMyNewsBean();
                if(mNewsBean == null){
                    Toast.makeText(getContext(),"newsBean == null",Toast.LENGTH_SHORT).show();
                    return;
                }
                questionTable = new QuestionTable();
                questionTable.setNewsId(mNewsBean);
                LogUtils.d("newsBean","mNewsBean title is " + mNewsBean.getTitle());
                String content = mMainQuestionET.getText().toString();
                String description = mQuestionDescriptionET.getText().toString();
                questionTable.setQuestion_content(content);
                questionTable.setQuestion_description(description);
                questionTable.setQuestionerId(UserBean.getCurrentUser(getContext(),UserBean.class));
                mPresenter.saveQuestionByPresenter(questionTable);
            }
        });
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
    public void saveQuestionSuccess() {
        Toast.makeText(getContext(),"提问成功",Toast.LENGTH_SHORT).show();
    }

    @Override
    public void saveQuestionFailure(int errorCode, String message) {
        Toast.makeText(getContext(),"提问失败，请稍后再试",Toast.LENGTH_SHORT).show();
    }
}

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
import com.example.asus.tastenews.beans.ReplyTable;
import com.example.asus.tastenews.beans.UserBean;
import com.example.asus.tastenews.news.presenter.CommentPresenter;
import com.rengwuxian.materialedittext.MaterialEditText;

import mehdi.sakout.fancybuttons.FancyButton;

/**
 * Created by SomeOneInTheWorld on 2016/8/25.
 */
public class CommentAddFragment extends BaseFragment{
    private MaterialEditText mCommentMET;
    private FancyButton mSubmitFB;
    private ProgressBar mProgressBar;

    public static final String EXTRA_USERBEAN = "userbean";
    public static final String EXTRA_QUESTIONTABLE = "questiontable";

    private CommentPresenter mPresenter;
    private QuestionTable mQuestionTable;
    private NewsBean mNewsBean;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_comment,container,false);

        Bundle bundle = getArguments();
        mQuestionTable = (QuestionTable)bundle.getSerializable(EXTRA_QUESTIONTABLE);
        mNewsBean = (NewsBean)bundle.getSerializable(EXTRA_USERBEAN);
        mPresenter = getPresenter();
        initLayout(view);

        return view;
    }

    private void initLayout(View view){
        mCommentMET = (MaterialEditText)view.findViewById(R.id.met_comment_to_question);
        mSubmitFB = (FancyButton)view.findViewById(R.id.fbt_comment_submit);
        mProgressBar = (ProgressBar)view.findViewById(R.id.progress);

        mSubmitFB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ReplyTable table = new ReplyTable();
                String content = mCommentMET.getText().toString();
                table.setReplyerId(UserBean.getCurrentUser(getContext(),UserBean.class));
                table.setReply_content(content);
                table.setQuestionId(mQuestionTable);
                mPresenter.saveQuestionAnswersByPresenter(table);
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
    public void saveQuestionAnswerSuccess() {
        Toast.makeText(getContext(), "回答成功", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void saveQuestionAnswerFailure(int errorCode, String message) {
        Toast.makeText(getContext(), "回答失败，请稍后再试", Toast.LENGTH_SHORT).show();
    }
}

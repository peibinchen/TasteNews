package com.example.asus.tastenews.news.widget;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.asus.tastenews.R;
import com.example.asus.tastenews.beans.QuestionTable;
import com.example.asus.tastenews.beans.ReplyTable;
import com.example.asus.tastenews.news.adapter.CardStackAdapter;
import com.example.asus.tastenews.news.presenter.CommentPresenter;
import com.example.asus.tastenews.utils.LogUtils;
import com.loopeer.cardstack.CardStackView;
import com.loopeer.cardstack.UpDownAnimatorAdapter;

import java.util.List;

/**
 * Created by ASUS on 2016/8/23.
 */
public class CommentAreaFragment extends BaseFragment implements CardStackAdapter.OnSwitch2CommentAddFragment{
    private CardStackView mMainView;
    private CardStackAdapter mAdapter;
    private ProgressBar mProgressBar;

    private boolean isQueryNewsBean = true;

    private String TAG = "COMMENT_TEST";

    private CommentPresenter mPresenter;

    public static Integer[] BACKGROUND_DATAS = new Integer[]{
            R.color.color_1,
            R.color.color_2,
            R.color.color_3,
            R.color.color_4,
            R.color.color_5,
            R.color.color_6,
            R.color.color_7,
            R.color.color_8,
            R.color.color_9,
            R.color.color_10,
            R.color.color_11,
            R.color.color_12,
            R.color.color_13,
            R.color.color_14,
            R.color.color_15,
            R.color.color_16,
            R.color.color_17,
            R.color.color_18,
            R.color.color_19,
            R.color.color_20,
            R.color.color_21,
            R.color.color_22,
            R.color.color_23,
            R.color.color_24,
            R.color.color_25,
            R.color.color_26
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_comment_area,container,false);
        mPresenter = getPresenter();
        initLayout(view);

        super.onCreateView(inflater,container,savedInstanceState);
        return view;
    }

    private void initLayout(View view){
        mMainView = (CardStackView)view.findViewById(R.id.stackview_main);
        mProgressBar = (ProgressBar)view.findViewById(R.id.progress);

        mAdapter = new CardStackAdapter(getContext(),this);
        mMainView.setAdapter(mAdapter);

        mMainView.setAnimatorAdapter(new UpDownAnimatorAdapter(mMainView));
    }

    @Override
    public void getReplyTableByQuestion(QuestionTable table) {
        mPresenter.showQuestionAnswersByPresenter(table);
    }

    @Override
    public void queryAnswersSuccess(List<ReplyTable>list){
        mAdapter.updateReplys(list);
    }

    @Override
    public void queryAnswersFailure(int errorCode,String message){
        Toast.makeText(getContext(),"queryAnswersFailure and errorCode = " + errorCode + "  message = " + message,Toast.LENGTH_LONG).show();
    }

    @Override
    public void switch2CommentAddFragment(QuestionTable table) {
        Bundle bundle = new Bundle();
        bundle.putSerializable(CommentAddFragment.EXTRA_USERBEAN,getMyNewsBean());
        bundle.putSerializable(CommentAddFragment.EXTRA_QUESTIONTABLE,table);
        CommentAddFragment fragment = new CommentAddFragment();
        fragment.setArguments(bundle);
        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_comment_content,fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    @Override
    public void showProgress() {
        mProgressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideProgress() {
        mProgressBar.setVisibility(View.GONE);
        if(isQueryNewsBean)
            mPresenter.showQuestionsByPresenter(getMyNewsBean());
        isQueryNewsBean = false;
    }

    @Override
    public void queryQuestionSuccess(List<QuestionTable> list) {
        if(list == null || list.size() == 0){
            Toast.makeText(getContext(),"暂无评论",Toast.LENGTH_SHORT).show();
            return;
        }
        mAdapter.updateData(list);
    }

    @Override
    public void queryQuestionFailure(int errorCode, String message) {
        LogUtils.d(TAG,"errorCode = " + errorCode + " and message is " + message);
    }
}

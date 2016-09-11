package com.example.asus.tastenews.news.model;

import android.content.Context;

import com.example.asus.tastenews.beans.NewsBean;
import com.example.asus.tastenews.beans.QuestionTable;
import com.example.asus.tastenews.beans.ReplyTable;
import com.example.asus.tastenews.beans.ReplyToReplyerTable;
import com.example.asus.tastenews.utils.LogUtils;

import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.datatype.BmobQueryResult;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SQLQueryListener;
import cn.bmob.v3.listener.SaveListener;

/**
 * Created by ASUS on 2016/8/24.
 */
public class CommentModelImpl implements CommentModel {
    private Context mContext;
    private OnQueryCallback mCallback;

    private String TAG = "COMMENTMODELTEST";

    public static final int QUESTION_PAGE_SIZE = 7;
    private int mQuestionPageIndex = 0;

    private final int ANSWER_PAGE_SIZE = 7;
    private int mAnswerPageIndex = 0;

    private final int REPLY2REPLY_PAGE_SIZE = 7;
    private int mReply2ReplyIndex = 0;

    public CommentModelImpl(Context context,OnQueryCallback callback){
        mContext = context;
        mCallback = callback;
    }

    @Override
    public void saveQuestionAnswers(ReplyTable table) {
        table.save(mContext, new SaveListener() {
            @Override
            public void onSuccess() {
                mCallback.onSaveQuestionAnswerSuccess();
            }

            @Override
            public void onFailure(int i, String s) {
                mCallback.onSaveQuestionAnswerFailure(i,s);
            }
        });
    }

    @Override
    public void saveQuestion(QuestionTable questionTable) {
        questionTable.save(mContext, new SaveListener() {
            @Override
            public void onSuccess() {
                mCallback.onQuestionSaveSuccess();
            }

            @Override
            public void onFailure(int i, String s) {
                mCallback.onQuestionSaveFailure(i,s);
            }
        });
    }

    /**
     * 由于呈现的控件是CardView，而这个控件目前还没发现能够对滑动停止进行判断，
     * 这和它里面没有使用ScrollView有一定关系，
     * 因此决定目前先全部加载出来，到后期数据庞大的时候再考虑是要替代控件还是重写控件
     * @param bean
     */
    @Override
    public void queryQuestionsForCommentArea(NewsBean bean) {
        BmobQuery<QuestionTable> query = new BmobQuery<>();
        query.setLimit(QUESTION_PAGE_SIZE);
        query.setSkip(mQuestionPageIndex);
        mQuestionPageIndex += QUESTION_PAGE_SIZE;

        LogUtils.d(TAG,"bean is " + bean);
        query.addWhereEqualTo("newsId",bean);
        query.include("newsId");
        query.include("questionerId");
        LogUtils.d(TAG,"query is " + query);
        query.findObjects(mContext, new FindListener<QuestionTable>() {
            @Override
            public void onSuccess(List<QuestionTable> list) {
                LogUtils.d(TAG,"list size = " + list.size());
                mCallback.onQuestionQuerySuccess(list);
            }

            @Override
            public void onError(int i, String s) {
                LogUtils.d(TAG,"onError and i = " + i + " and s = " + s);
                mCallback.onQuestionQueryFailure(i,s);
            }
        });
    }

    @Override
    public void queryQuestionAnswers(QuestionTable questionTable) {
        BmobQuery<ReplyTable> query = new BmobQuery<>();
        query.setLimit(ANSWER_PAGE_SIZE);
        query.setSkip(mQuestionPageIndex);
        mQuestionPageIndex += ANSWER_PAGE_SIZE;

        query.addWhereEqualTo("questionId",questionTable);
        query.include("questionId");
        query.include("replyerId");
        query.findObjects(mContext, new FindListener<ReplyTable>() {
            @Override
            public void onSuccess(List<ReplyTable> list) {
                LogUtils.d(TAG,"reply list size = " + list.size());
                mCallback.onReplyQuerySuccess(list);
            }

            @Override
            public void onError(int i, String s) {
                mCallback.onReplyQueryFailure(i,s);
            }
        });
    }

    @Override
    public void questionReplyToAnswers(ReplyTable replyTable) {
        BmobQuery<ReplyToReplyerTable> query = new BmobQuery<>();
        query.setLimit(REPLY2REPLY_PAGE_SIZE);
        query.setSkip(mReply2ReplyIndex);
        mReply2ReplyIndex += REPLY2REPLY_PAGE_SIZE;

        query.addWhereEqualTo("replyId",replyTable);
        query.include("replyId");
        query.findObjects(mContext, new FindListener<ReplyToReplyerTable>() {
            @Override
            public void onSuccess(List<ReplyToReplyerTable> list) {
                mCallback.onReply2ReplyerQuerySuccess(list);
            }

            @Override
            public void onError(int i, String s) {
                mCallback.onReply2ReplyerQueryFailure(i, s);
            }
        });
    }

    @Override
    public void queryNewsBean(String docid) {
        String bql = "select * from NewsBean where docid = " + "'" + docid + "'";
        BmobQuery<NewsBean> query = new BmobQuery<>();
        query.setSQL(bql);
        query.doSQLQuery(mContext, new SQLQueryListener<NewsBean>() {
            @Override
            public void done(BmobQueryResult<NewsBean> bmobQueryResult, BmobException e) {
                if(e == null){
                    if(bmobQueryResult.getResults().size() == 0){
                        mCallback.onQueryNewsBeanFailure("query size is zero");
                        return;
                    }

                    NewsBean bean = bmobQueryResult.getResults().get(0);
                    if(bean == null){
                        mCallback.onQueryNewsBeanFailure("bean == null");
                        return;
                    }

                    mCallback.onQueryNewsBeanSuccess(bean);
                }else{
                    mCallback.onQueryNewsBeanFailure(e.getMessage());
                }
            }
        });
    }

    @Override
    public void saveNewsBean(final NewsBean bean) {
        bean.save(mContext, new SaveListener() {
            @Override
            public void onSuccess() {
                mCallback.onSaveNewsBeanSuccess(bean);
            }

            @Override
            public void onFailure(int i, String s) {
                mCallback.onSaveNewsBeanFailure(i,s);
            }
        });
    }

    public interface OnQueryCallback{
        void onQuestionSaveSuccess();
        void onQuestionSaveFailure(int errorCode,String message);

        void onQuestionQuerySuccess(List<QuestionTable> list);
        void onQuestionQueryFailure(int errorCode,String message);

        void onReplyQuerySuccess(List<ReplyTable>list);
        void onReplyQueryFailure(int errorCode,String message);

        void onReply2ReplyerQuerySuccess(List<ReplyToReplyerTable>list);
        void onReply2ReplyerQueryFailure(int errorCode,String message);

        void onQueryNewsBeanSuccess(NewsBean bean);
        void onQueryNewsBeanFailure(String message);

        void onSaveNewsBeanSuccess(NewsBean bean);
        void onSaveNewsBeanFailure(int errorCode,String message);

        void onSaveQuestionAnswerSuccess();
        void onSaveQuestionAnswerFailure(int errorCode,String message);
    }
}

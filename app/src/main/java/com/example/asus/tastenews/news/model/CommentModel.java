package com.example.asus.tastenews.news.model;

import com.example.asus.tastenews.beans.NewsBean;
import com.example.asus.tastenews.beans.QuestionTable;
import com.example.asus.tastenews.beans.ReplyTable;
import com.example.asus.tastenews.beans.ReplyToReplyerTable;

import java.util.List;

/**
 * Created by SomeOneInTheWorld on 2016/8/24.
 */
public interface CommentModel {
    interface OnQueryCallback{
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

    void saveQuestion(QuestionTable questionTable);
    void saveQuestionAnswers(ReplyTable table);
    void queryQuestionsForCommentArea(NewsBean bean);
    void queryQuestionAnswers(QuestionTable questionTable);
    void questionReplyToAnswers(ReplyTable replyTable);
    void queryNewsBean(String docid);
    void saveNewsBean(NewsBean bean);
}

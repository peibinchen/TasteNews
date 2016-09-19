package com.example.asus.tastenews.news.presenter;

import com.example.asus.tastenews.beans.NewsBean;
import com.example.asus.tastenews.beans.QuestionTable;
import com.example.asus.tastenews.beans.ReplyTable;

/**
 * Created by SomeOneInTheWorld on 2016/8/24.
 */
public interface CommentPresenter {
    void saveQuestionByPresenter(QuestionTable table);
    void showQuestionsByPresenter(NewsBean newsBean);
    void saveQuestionAnswersByPresenter(ReplyTable table);
    void showQuestionAnswersByPresenter(QuestionTable table);
    void showQuestionReplyToAnswerByPresenter(ReplyTable replyTable);
    void queryNewsBeanByPresenter(String docid,NewsBean bean);
}

package com.example.asus.tastenews.news.model;

import com.example.asus.tastenews.beans.NewsBean;
import com.example.asus.tastenews.beans.QuestionTable;
import com.example.asus.tastenews.beans.ReplyTable;

/**
 * Created by ASUS on 2016/8/24.
 */
public interface CommentModel {
    void saveQuestion(QuestionTable questionTable);
    void saveQuestionAnswers(ReplyTable table);
    void queryQuestionsForCommentArea(NewsBean bean);
    void queryQuestionAnswers(QuestionTable questionTable);
    void questionReplyToAnswers(ReplyTable replyTable);
    void queryNewsBean(String docid);
    void saveNewsBean(NewsBean bean);
}

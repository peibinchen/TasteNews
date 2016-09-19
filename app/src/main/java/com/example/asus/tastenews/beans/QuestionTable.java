package com.example.asus.tastenews.beans;

import java.io.Serializable;

import cn.bmob.v3.BmobObject;

/**
 * Created by SomeOneInTheWorld on 2016/8/22.
 */
public class QuestionTable extends BmobObject implements Serializable{
    private UserBean questionerId;
    private Integer questionId;
    private NewsBean newsId;
    private String question_content;
    private String question_description;

    public void setQuestion_description(String description){
        question_description = description;
    }
    public String getQuestion_description(){
        return question_description;
    }

    public UserBean getQuestionerId(){
        return questionerId;
    }
    public void setQuestionerId(UserBean user){
        questionerId = user;
    }

    public Integer getQuestionId(){
        return questionId;
    }
    public void setQuestionId(Integer questionId){
        this.questionId = questionId;
    }

    public NewsBean getNewsId(){
        return newsId;
    }
    public void setNewsId(NewsBean n){
        this.newsId = n;
    }

    public String getQuestion_content(){
        return question_content;
    }
    public void setQuestion_content(String content){
        question_content = content;
    }
}

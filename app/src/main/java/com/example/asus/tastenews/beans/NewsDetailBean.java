package com.example.asus.tastenews.beans;

import java.io.Serializable;
import java.util.List;

/**
 * Created by SomeOneInTheWorld on 2016/5/24.
 */
public class NewsDetailBean implements Serializable {
    private String docid;
    private String title;
    private String source;
    private String body;
    private String ptime;
    private String cover;
    private List<String> imgList;//图片列表

    public String getDocid(){
        return docid;
    }
    public void setDocid(String docid){
        this.docid = docid;
    }

    public String getTitle(){
        return title;
    }
    public void setTitle(String title){
        this.title = title;
    }

    public String getSource(){
        return source;
    }
    public void setSource(String source){
        this.source = source;
    }

    public String getBody(){
        return body;
    }
    public void setBody(String body){
        this.body = body;
    }

    public String getPtime(){
        return ptime;
    }
    public void setPtime(String ptime){
        this.ptime = ptime;
    }

    public String getCover(){
        return cover;
    }
    public void setCover(String cover){
        this.cover = cover;
    }

    public List<String>getImgList(){
        return imgList;
    }
    public void setImgList(List<String>imgList){
        this.imgList = imgList;
    }
}

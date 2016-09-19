package com.example.asus.tastenews.beans;

import java.io.Serializable;

import cn.bmob.v3.BmobObject;

/**
 * Created by SomeOneInTheWorld on 2016/5/24.
 */
public class NewsBean extends BmobObject implements Serializable {
    private String docid;//docid
    private String title;//标题
    private String digest;//小内容
    private String source;//来源
    private String imgsrc;//图片地址
    private String ptime;//时间
    private String tag;//TAG

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

    public String getDigest(){
        return digest;
    }
    public void setDigest(String digest){
        this.digest = digest;
    }

    public String getSource(){
        return source;
    }
    public void setSource(String source){
        this.source = source;
    }

    public String getImgsrc(){
        return imgsrc;
    }
    public void setImgsrc(String imgsrc){
        this.imgsrc = imgsrc;
    }

    public String getPtime(){
        return ptime;
    }
    public void setPtime(String ptime){
        this.ptime = ptime;
    }

    public String getTag(){
        return tag;
    }
    public void setTag(String tag){
        this.tag = tag;
    }
}

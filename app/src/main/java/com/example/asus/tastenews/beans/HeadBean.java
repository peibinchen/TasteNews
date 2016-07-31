package com.example.asus.tastenews.beans;

/**
 * Created by ASUS on 2016/6/12.
 */
public class HeadBean {
    private String folderName;
    private String firstImagePath;
    private int imageCount;

    public void setFolderName(String folderName){
        this.folderName = folderName;
    }
    public String getFolderName(){
        return folderName;
    }

    public void setFirstImagePath(String firstImagePath){
        this.firstImagePath = firstImagePath;
    }
    public String getFirstImagePath(){
        return firstImagePath;
    }

    public void setImageCount(int imagecount){
        this.imageCount = imagecount;
    }
    public int getImageCount(){
        return imageCount;
    }
}

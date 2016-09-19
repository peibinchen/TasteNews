package com.example.asus.tastenews.news;

import com.example.asus.tastenews.beans.NewsBean;
import com.example.asus.tastenews.beans.NewsDetailBean;
import com.example.asus.tastenews.utils.JsonUtils;
import com.example.asus.tastenews.utils.LogUtils;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by SomeOneInTheWorld on 2016/5/24.
 */

/**
 * 帮助解析Json数据到News和NewsDetail中的类
 */
public class NewsJsonUtils {

    private static final String TAG = "NewsJsonUtils";

    /**
     * 解析json数据保存到NewsBean列表中
     * @param res
     * @param value
     * @return
     */
    public static List<NewsBean> readJsonNewsBean(String res,String value){
        LogUtils.d("TAGGG","response is " + res);
        List<NewsBean> beans = new ArrayList<>();
        try{
            JsonParser parser = new JsonParser();
            JsonObject object = parser.parse(res).getAsJsonObject();
            JsonElement jsonElement = object.get(value);
            if(jsonElement == null){
                return null;
            }

            JsonArray jsonArray = jsonElement.getAsJsonArray();
            for(int i=1;i<jsonArray.size();i++){
                JsonObject jo = jsonArray.get(i).getAsJsonObject();
                if (jo.has("skipType") && "special".equals(jo.get("skipType").getAsString())) {
                    continue;
                }
                if(jo.has("TAGS") && !jo.has("TAG")){
                    continue;
                }

                if(!jo.has("imgextra")){
                    NewsBean news = JsonUtils.deserialize(jo,NewsBean.class);
                    beans.add(news);
                }
            }
        }catch(Exception e){
            LogUtils.e(TAG,"readJsonNewsBean error",e);
        }

        return beans;
    }

    /**
     * 解析json数据到NewsDetailBean中
     * @param res
     * @param docid
     * @return
     */
    public static NewsDetailBean readJsonNewsDetailBeans(String res,String docid){
        NewsDetailBean bean = new NewsDetailBean();
        try{
            JsonParser parser = new JsonParser();
            JsonObject object = parser.parse(res).getAsJsonObject();
            JsonElement element = object.get(docid);
            if(element == null){
                return null;
            }

            bean = JsonUtils.deserialize(element.getAsJsonObject(),NewsDetailBean.class);
        }catch(Exception e){
            LogUtils.e(TAG,"readJsonNewsDetailBeans error",e);
        }

        return bean;
    }
}

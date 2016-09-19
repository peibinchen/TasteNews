package com.example.asus.tastenews.images;

import com.example.asus.tastenews.beans.ImageBean;
import com.example.asus.tastenews.utils.JsonUtils;
import com.example.asus.tastenews.utils.LogUtils;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by SomeOneInTheWorld on 2016/5/29.
 */
public class ImageJsonUtils {

    private static final String TAG = "ImageJsonUtils";

    public static List<ImageBean> readJsonImageBeans(String res){
        List<ImageBean>beans = new ArrayList<>();
        try{
            JsonParser parser = new JsonParser();
            JsonElement element = parser.parse(res).getAsJsonObject().get("showapi_res_body").getAsJsonObject().get("list");
            JsonArray jsonArray = element.getAsJsonArray();
            for(int i=1;i<jsonArray.size();i++){
                JsonObject jo = jsonArray.get(i).getAsJsonObject();
                ImageBean news = JsonUtils.deserialize(jo,ImageBean.class);
                beans.add(news);
            }
        }catch(Exception e){
            LogUtils.e(TAG,"readJsonImageBeans error",e);
        }

        return beans;
    }
}

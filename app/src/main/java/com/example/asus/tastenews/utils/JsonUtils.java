package com.example.asus.tastenews.utils;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;

import java.lang.reflect.Type;

/**
 * Created by ASUS on 2016/5/24.
 */
public class JsonUtils {
    public static Gson mGson = new Gson();

    /**
     * 将object转为json字符串
     * @param object
     * @param <T>
     * @return
     */
    public static<T>String serialize(T object){
        return mGson.toJson(object);
    }

    /**
     * 将json字符串转化为Class<T>格式
     * @param json
     * @param clz
     * @param <T>
     * @return
     * @throws JsonSyntaxException
     */
    public static<T>T deserialize(String json,Class<T>clz)throws JsonSyntaxException {
        return mGson.fromJson(json,clz);
    }

    /**
     * 将json对象转化为Class<T>类型
     * @param json
     * @param clz
     * @param <T>
     * @return
     * @throws JsonSyntaxException
     */
    public static<T>T deserialize(JsonObject json, Class<T>clz)throws JsonSyntaxException{
        return mGson.fromJson(json,clz);
    }

    /**
     * 将json对象转化为反射类型
     * @param json
     * @param type
     * @param <T>
     * @return
     * @throws JsonSyntaxException
     */
    public static<T>T deserialize(JsonObject json,Type type)throws JsonSyntaxException{
        return mGson.fromJson(json,type);
    }

    public static<T>T deserialize(String json,Type type)throws JsonSyntaxException{
        return mGson.fromJson(json,type);
    }
}

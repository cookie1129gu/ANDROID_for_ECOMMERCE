package com.example.androidforecommerce.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.lang.reflect.Type;

public class JSONUtills {
    private static Gson gson=new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();

    public static <T>T fromJson(String json,Class<T> clz){
        return gson.fromJson(json,clz);
    }

    public static <T>T fromJson(String json, Type type){
        return gson.fromJson(json,type);
    }

    public static String toJSON(Object obj){
        return gson.toJson(obj);
    }

    public static Gson getGson(){
        return gson;
    }
}

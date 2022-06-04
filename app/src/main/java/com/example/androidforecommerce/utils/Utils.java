package com.example.androidforecommerce.utils;

import android.content.Context;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.WindowManager;

/*
* 用于像素转换，获得界面宽度高度的工具类
* */
public class Utils {

    /*获取屏幕宽度*/
    public static int getScreenWidth(Context context){
        DisplayMetrics displayMetrics=new DisplayMetrics();
        WindowManager windowManager=(WindowManager)context.getSystemService(Context.WINDOW_SERVICE);
        Display display= windowManager.getDefaultDisplay();
        display.getMetrics(displayMetrics);
        return displayMetrics.widthPixels;
    }

    /*获取屏幕高度*/
    public static int getScreenHeight(Context context){
        DisplayMetrics displayMetrics=new DisplayMetrics();
        WindowManager windowManager=(WindowManager)context.getSystemService(Context.WINDOW_SERVICE);
        Display display= windowManager.getDefaultDisplay();
        display.getMetrics(displayMetrics);
        return displayMetrics.heightPixels;
    }

    /*
    dp转px
    */
    public static int dp2px(Context context,float dpValue){
        final float scale=context.getResources().getDisplayMetrics().density;
        return (int)(dpValue*scale+0.5f);
    }

}

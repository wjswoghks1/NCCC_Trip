package com.exam.administrator.nccc_trip;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

import java.util.ArrayList;

/**
 * Created by han on 2015-11-24.
 */
public class staticMerge {
    static String temp="clear";
    static String what="nothing";
    static String si;
    static String dong;
    static String bunji;
    static double latitude;
    static double longitude;
    static String[] finish_food = new String[10];
    static String[] recommendation_category = new String[10];
    static ArrayList<String> food = new ArrayList<>();
    static ArrayList<String> foodAnni = new ArrayList<>();
    static boolean timer = false;


    static {
        loadAddr(MainActivity.mContext);
    }


    public static void idTotemp (int i) {
        if(200<=i && i<300) {
            temp = "thunderstorm";
        } else if(500<=i && i<600) {
            temp = "rain";
        } else if(600<=i && i<700){
            temp = "snow";
        } else if ( i==761) {
            temp = "dust";
        } else if(i == 800) {
            temp = "clear";
        } else if(800<i && i<900) {
            temp = "clouds";
        } else if(i == 903) {
            temp = "cold";
        } else if(i==904) {
            temp = "hot";
        } else if(i==905) {
            temp = "windy";
        } else if(i==902) {
            temp = "hurricane";
        } else if(i == 960) {
            temp = "storm";
        }
    }

    public static void saveAddr(Context context, String Si, String Dong, String Bunji) {
        SharedPreferences pref = context.getSharedPreferences("staticMerge", Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.remove("si").commit();
        editor.putString("si", Si);
        editor.remove("dong").commit();
        editor.putString("dong", Dong);
        editor.remove("bunji").commit();
        editor.putString("bunji", Bunji);

        editor.commit();
    }

    public static void loadAddr(Context context) {
        SharedPreferences pref = context.getSharedPreferences("staticMerge", Activity.MODE_PRIVATE);
        si = pref.getString("si","");
        dong = pref.getString("dong","");
        bunji = pref.getString("bunji","");
    }


}


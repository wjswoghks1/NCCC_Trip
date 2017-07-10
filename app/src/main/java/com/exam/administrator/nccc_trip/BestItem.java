package com.exam.administrator.nccc_trip;

import android.graphics.Bitmap;

/**
 * Created by Administrator on 2017-07-08.
 */

public class BestItem {
    private String name;
    private Bitmap imgUrl;

    public BestItem(String name, Bitmap imgUrl){
        this.name = name;
        this.imgUrl = imgUrl;
    }
    public String getName(){
        return name;
    }
    public Bitmap getImgUrl(){
        return imgUrl;
    }
}

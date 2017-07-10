package com.exam.administrator.nccc_trip;

import android.graphics.Bitmap;

/**
 * Created by Administrator on 2017-07-09.
 */

public class TourItem {

    private String name;
    private String address;
    private String distance;
    private Bitmap img;
    private int medal;

    public TourItem(String name, String address, String distance, Bitmap img, int medal){
        this.name = name;
        this.address = address;
        this.distance = distance;
        this.img = img;
        this.medal = medal;
    }
    public TourItem(String name, String address, String distance, Bitmap img){
        this.name = name;
        this.address = address;
        this.distance = distance;
        this.img = img;
        medal = 0;
    }
    public TourItem(String name, Bitmap img){
        this.name = name;
        this.img = img;
    }
    public String getName(){
        return name;
    }
    public String getAddress(){
        return address;
    }
    public String getDistance(){
        return distance;
    }
    public Bitmap getImg(){
        return img;
    }
    public int getMedal(){
        return medal;
    }
}
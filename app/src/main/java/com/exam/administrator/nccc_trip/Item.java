package com.exam.administrator.nccc_trip;

import java.io.Serializable;

/**
 * Created by han on 2015-11-24.
 */
public class Item implements Serializable {
    public String title;
    public String imageUrl;
    public String address;
    public String newAddress;
    public String zipcode;
    public String phone;
    public double longitude;
    public double latitude;
    public double distance;
    public String category;
    public String id;
    public String placeUrl;
    public String direction;
    public String addressBCode;
    public int seq;

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public void setSeq(int seq) {
        this.seq = seq;
    }

    public int getSeq() {
        return seq;
    }
}
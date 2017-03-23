package com.commuker.pocketmbta.datamodel;

/**
 * Created by lyu on 2017/3/22.
 */

public class Place {

    private String name;
    // In form of Map API: https://developers.google.com/maps/documentation/directions/intro#https-or-http
    // 24+Sussex+Drive+Ottawa+ON
    private String address;
    private float latitude;
    private float longtitude;

    public Place() {
    }

    public Place(String name, String address, float latitude, float longtitude) {
        this.name = name;
        this.address = address;
        this.latitude = latitude;
        this.longtitude = longtitude;
    }

    public String getName() {
        return name;
    }

    public String getAddress() {
        return address;
    }

    public float getLatitude() {
        return latitude;
    }

    public float getLongtitude() {
        return longtitude;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setLatitude(float latitude) {
        this.latitude = latitude;
    }

    public void setLongtitude(float longtitude) {
        this.longtitude = longtitude;
    }
}

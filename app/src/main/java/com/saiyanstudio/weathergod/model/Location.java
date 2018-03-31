package com.saiyanstudio.weathergod.model;

import java.io.Serializable;

/**
 * Created by root on 23/5/15.
 */
public class Location implements Serializable{
    private int id;
    private String locationName;
    private String countryName;

    public Location(){}

    public Location(String locationName,String countryName){
        this.locationName = locationName;
        this.countryName = countryName;
    }

    public Location(int id,String locationName,String countryName){
        this.id = id;
        this.locationName = locationName;
        this.countryName = countryName;
    }

    public void setId(int id){
        this.id = id;
    }

    public void setLocationName(String locationName){
        this.locationName = locationName;
    }
    public void setCountryName(String countryName){
        this.countryName = countryName;
    }

    public int getId(){ return id; }

    public String getLocationName() {
        return locationName;
    }

    public String getCountryName() {
        return countryName;
    }

    public void add(String location,String country){
        this.locationName = location;
        this.countryName = country;
    }
}

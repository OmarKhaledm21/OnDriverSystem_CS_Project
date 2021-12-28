package com.ondriver;
import java.util.ArrayList;

public class Area {
    private String location;
    private ArrayList<Captain> pinnedCaptain;
    private double discount;

    public Area(String location){
        this.discount = 0.0;
        this.location = location;
        this.pinnedCaptain = new ArrayList<>();
    }

    public String getLocation() {
        return location;
    }

    public void addToPinnedDrivers(Captain captain){
        this.pinnedCaptain.add(captain);
    }

    public boolean isFavouriteDriver(Captain captain){
        return pinnedCaptain.contains(captain);
    }

    public double getDiscount() {
        return discount;
    }

    public void setDiscount(double discount) {
        this.discount = discount;
    }

    @Override
    public String toString() {
        return location;
    }
}

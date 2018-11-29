package com.example.brandonezz.assignment_maps_brandonezzedine;

public class MapLocation {

    private String location;
    private double latitude;
    private double longitude;

    public MapLocation() {
    }

    public MapLocation(String location, double latitude, double longitude) {
        this.location = location;
        this.latitude = latitude;
        this.longitude = longitude;
    }


    public String getLocation() {
        return location;
    }

    public double getLatitude() { return latitude; }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }
}


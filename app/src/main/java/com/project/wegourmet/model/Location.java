package com.project.wegourmet.model;
public class Location {
    private final String streetAddress;
    private final String cityName;
    private final Coordinate coordinate;

    public Location(String streetAddress, String cityName, Coordinate coordinate) {
        this.streetAddress = streetAddress;
        this.cityName = cityName;
        this.coordinate = coordinate;
    }

    public String getStreetAddress() {
        return streetAddress;
    }

    public String getCityName() {
        return cityName;
    }

    public Coordinate getCoordinates() {
        return coordinate;
    }




}
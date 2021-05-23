package com.unileipzig.shop;

public class Dvd extends Product {

    private String format;
    private int durationMinutes;
    private short regionCode;

    Dvd(int prodNumber, String title) {
        super(prodNumber, title);
    }

    public String getFormat() {
        return format;
    }

    public void setFormat(String format) {
        this.format = format;
    }

    public int getDurationMinutes() {
        return durationMinutes;
    }

    public void setDurationMinutes(int durationMinutes) {
        this.durationMinutes = durationMinutes;
    }

    public short getRegionCode() {
        return regionCode;
    }

    public void setRegionCode(short regionCode) {
        this.regionCode = regionCode;
    }
}

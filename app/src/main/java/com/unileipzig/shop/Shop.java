package com.unileipzig.shop;

public class Shop {
    String name;
    String street;
    String zip;

    public Shop(String name, String street, String zip) {
        this.name = name;
        this.street = street;
        this.zip = zip;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getZip() {
        return zip;
    }

    public void setZip(String zip) {
        this.zip = zip;
    }
}

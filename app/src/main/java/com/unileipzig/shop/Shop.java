package com.unileipzig.shop;

/**
 * model for class store
 */
public class Shop {

    private String name;
    private String street;
    private String zip;

    Shop(String name, String street, String zip) {
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

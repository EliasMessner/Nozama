package com.unileipzig.shop.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class Store {

    @Id
    @Column(name = "s_name")
    private String name;

    @Column(nullable = false)
    private String street;

    @Column(nullable = false)
    private String zip;

    public Store(){};

    public Store(String name, String street, String zip) {
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


package com.unileipzig.shop;

public abstract class Product {

    int prodNumber;
    String title;
    double rating;
    int salesRank;
    String image;

    Product(int prodNumber, String title) {
        this.prodNumber = prodNumber;
        this.title = title;
    }

    public int getProdNumber() {
        return prodNumber;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public int getSalesRank() {
        return salesRank;
    }

    public void setSalesRank(int salesRank) {
        this.salesRank = salesRank;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}

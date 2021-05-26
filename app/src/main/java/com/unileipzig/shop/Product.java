package com.unileipzig.shop;

public abstract class Product {

    String prodNumber;
    String title;
    double rating;
    int salesRank;
    String image;

    Product(String prodNumber, String title) {
        this.prodNumber = prodNumber;
        this.title = title;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Product)) {
            return super.equals(o);
        }
        Product other = (Product) o;
        return this.prodNumber.equals(other.getProdNumber())
                && this.title.equals(other.getTitle())
                && this.rating == other.getRating()
                && (this.image == null || other.getImage() == null || this.image.equals(other.getImage()))  // because img can be null
                && this.salesRank == (other.getSalesRank());
    }

    public String getProdNumber() {
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

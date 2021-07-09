package com.unileipzig.shop.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table( name = "product")
public class Product {

    @Id
    @Column(name = "prod_number")
    private String prodNumber;
    private String title;
    private double rating;
    @Column(name = "sales_rank")
    private Integer salesRank;
    private String image;

    /**
     * Constructor for Hibernate
     */
    public Product(){};

    /**
     * Constructs a product with initial rating of 3.0, specified title and product number.
     * @param prodNumber the product number for identification, serving as the key in the database and asin in th xml files.
     * @param title the title of the product
     */
    public Product(String prodNumber, String title) {
        this.prodNumber = prodNumber;
        this.title = title;
    }

    public String getProdNumber() {
        return prodNumber;
    }

    public void setProdNumber(String prodNumber) {
        this.prodNumber = prodNumber;
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

    public Integer getSalesRank() {
        return salesRank;
    }

    public void setSalesRank(Integer salesRank) {
        this.salesRank = salesRank;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}

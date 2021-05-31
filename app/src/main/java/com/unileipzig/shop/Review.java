package com.unileipzig.shop;

import java.time.LocalDate;

public class Review {

    private Customer customer;
    private Product product;
    private LocalDate date;
    private Integer stars;
    private String summary;
    private String details;

    Review (Customer customer, Product product, Integer stars){
        this.customer = customer;
        this.product = product;
        this.stars = stars;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public Integer getStars() {
        return stars;
    }

    public void setStars(Integer stars) {
        this.stars = stars;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }
}

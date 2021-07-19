package com.unileipzig.shop.model;

import org.hibernate.annotations.Check;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@DynamicInsert
@DynamicUpdate
public class Review {

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private long id;

    @ManyToOne()
    @JoinTable(name = "customer", joinColumns = @JoinColumn(name = "username"))
    private Customer customer;

    @ManyToOne()
    @JoinTable(name = "product", joinColumns = @JoinColumn(name = "prod_number"))
    private Product product;

    @Check(constraints = "date <= CURRENT_DATE")
    private LocalDate date;

    @Column(nullable = false)
    private Integer stars;

    private String summary;

    private String details;

    /**
     * Constructor for Hibernate
     */
    public Review() {}

    public Review (Customer customer, Product product, Integer stars){
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

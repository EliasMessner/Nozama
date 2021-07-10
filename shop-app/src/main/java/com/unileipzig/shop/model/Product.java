package com.unileipzig.shop.model;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@DynamicInsert
@DynamicUpdate
@Inheritance(strategy = InheritanceType.JOINED)
public class Product {

    @Id
    private String prodNumber;

    @Column(nullable = false)
    private String title;

    @ColumnDefault("3")
    private Double rating;

    private Integer salesRank;

    private String image;

    @ManyToMany()
    @Cascade( { org.hibernate.annotations.CascadeType.SAVE_UPDATE })
    @JoinTable(name = "similar_products", joinColumns = @JoinColumn(name = "product1"), inverseJoinColumns =
    @JoinColumn(name = "product2"))
    private List<Product> similarProducts;

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

        this.similarProducts = new ArrayList<>();
    }

    @Override
    public boolean equals(Object obj) {
        return super.equals(obj) || this.prodNumber.equals(((Product) obj).prodNumber);
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

    public Double getRating() {
        return rating;
    }

    public void setRating(Double rating) {
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

    public List<Product> getSimilarProducts() {
        return similarProducts;
    }

    public void setSimilarProducts(List<Product> similarProducts) {
        this.similarProducts = similarProducts;
    }

    public void addSimilarProduct(Product similarProduct) {
        this.similarProducts.add(similarProduct);
    }
}

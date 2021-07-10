package com.unileipzig.shop.model;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "store_inventory")
public class Offer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    @JoinColumn(name = "product")
    private Product product;

    @ManyToOne
    @JoinColumn(name = "store_name")
    private Store store;

    private String articleCondition;

    private BigDecimal price;

    public Offer(){};

    public Offer(Product product, Store store) {
        this.product = product;
        this.store = store;
    }

    @Override
    public boolean equals(Object obj) {
        return super.equals(obj) || this.id == ((Offer) obj).id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Product getProduct() {
        return product;
    }

    public Store getShop() {
        return store;
    }

    public String getArticleCondition() {
        return articleCondition;
    }

    public void setArticleCondition(String articleCondition) {
        this.articleCondition = articleCondition;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }
}

package com.unileipzig.shop.model;

import java.math.BigDecimal;

/**
 * model for table store_inventory
 */
public class Offer {

    private int id;
    private Product product;
    private Shop shop;
    private String articleCondition;
    private BigDecimal price;

    public Offer(Product product, Shop shop) {
        this.product = product;
        this.shop = shop;
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

    public Shop getShop() {
        return shop;
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

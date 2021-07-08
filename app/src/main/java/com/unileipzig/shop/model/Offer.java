package com.unileipzig.shop.model;

import java.math.BigDecimal;

/**
 * model for table store_inventory
 */
public class Offer {

    private int artId;
    private Product product;
    private Shop shop;
    private String articleCondition;
    private BigDecimal price;

    public Offer(Product product, Shop shop) {
        this.product = product;
        this.shop = shop;
    }

    public int getArtId() {
        return artId;
    }

    public void setArtId(int artId) {
        this.artId = artId;
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

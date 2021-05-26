package com.unileipzig.shop;

import java.math.BigDecimal;

public class Offer {

    private int artId;
    private Product product;
    private Shop shop;
    private String articleCondition;
    private BigDecimal price;

    Offer(Product product, Shop shop) {
        this.product = product;
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

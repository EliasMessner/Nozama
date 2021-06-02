package com.unileipzig.shop;

import static com.unileipzig.shop.CompareUtil.alphanumericallyEquals;
import static com.unileipzig.shop.CompareUtil.equalsAllowNull;

/**
 * model for table product
 */
public class Product {

    private String prodNumber;
    private String title;
    private double rating;
    private int salesRank;
    private String image;

    /**
     * Constructs a product with initial rating of 3.0, specified title and product number.
     * @param prodNumber the product number for identification, serving as the key in the database and asin in th xml files.
     * @param title the title of the product
     */
    Product(String prodNumber, String title) {
        this.prodNumber = prodNumber;
        this.title = title;
        this.rating = 3.0;
    }

    /**.
     * @param o the Object to compare this product to
     * @return true if the titles of both are equal with regard to only alphanumerical characters, and null values for
     * optional attribute image don't violate equality
     */
    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Product)) {
            return super.equals(o);
        }
        Product other = (Product) o;
        return this.prodNumber.equals(other.getProdNumber())
                && alphanumericallyEquals(title, other.getTitle())
                && rating == other.getRating()
                && equalsAllowNull(image, other.getImage())  // because img can be null
                && salesRank == (other.getSalesRank());
                // ignore all special characters and whitespaces when comparing titles because they might be formatted
                // incorrectly. When prodNumber matches and title matches except for special characters, we can assume
                // they are the same product
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

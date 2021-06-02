package com.unileipzig.shop;

import org.xml.sax.Attributes;

import java.io.IOException;
import java.sql.Connection;

/**
 * Concrete subclass of SimilarProductsHandler, adapted for the dresden xml file.
 */
public class SimilarProductsHandlerDresden extends SimilarProductsHandler {

    /**
     * {@inheritDoc}
     */
    public SimilarProductsHandlerDresden(Connection conn, String errorFilePath) throws IOException {
        super(conn, errorFilePath);
    }

    /**
     * Checks if the current tag is in the "similars" tag, if so, sets the similar-relation that is described by the
     * current tag, otherwise, sets the product number of the current product to the asin value in this tag.
     * @param attributes the attributes of the current start tag.
     */
    @Override
    protected void handleItemStartTag(Attributes attributes) {
        if (isInSimilars) {
            addSimilarRelation(product_number, attributes.getValue("asin"));
        } else {
            product_number = attributes.getValue("asin");
        }
    }

}

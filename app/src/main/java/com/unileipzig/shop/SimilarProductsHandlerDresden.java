package com.unileipzig.shop;

import org.xml.sax.Attributes;

import java.io.IOException;
import java.sql.Connection;

public class SimilarProductsHandlerDresden extends SimilarProductsHandler {

    public SimilarProductsHandlerDresden(Connection conn) throws IOException {
        super(conn);
    }

    @Override
    protected void handleItemStartTag(Attributes attributes) {
        if (isInSimilars) {
            addSimilarRelation(product_number, attributes.getValue("asin"));
        } else {
            product_number = attributes.getValue("asin");
        }
    }

}

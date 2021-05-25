package com.unileipzig.shop;

import org.xml.sax.Attributes;

import java.io.IOException;
import java.sql.Connection;

public class SimilarProductsHandlerLeipzig extends SimilarProductsHandler {

    protected boolean isInSimProduct;

    public SimilarProductsHandlerLeipzig(Connection conn) throws IOException {
        super(conn);
        isInSimProduct = false;
    }

    @Override
    protected void handleItemStartTag(Attributes attributes) {
        product_number = attributes.getValue("asin");
    }

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) {
        super.startElement(uri, localName, qName, attributes);
        if (qName.equals("sim_product")) {
            isInSimProduct = true;
        }
    }

    @Override
    public void endElement(String uri, String localName, String qName) {
        super.endElement(uri, localName, qName);
        if (qName.equals("sim_product")) {
            isInSimProduct = false;
        } else if (qName.equals("asin")) {
            handleAsinEndTag();
        }
    }

    private void handleAsinEndTag() {
        if (isInSimProduct) {
            addSimilarRelation(product_number, currentValue.toString());
        }
    }
}

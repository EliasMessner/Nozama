package com.unileipzig.shop;

import org.xml.sax.Attributes;

import java.io.IOException;
import java.sql.Connection;

/**
 * Concrete subclass of SimilarProductsHandler, adapted for the leipzig xml file.
 */
public class SimilarProductsHandlerLeipzig extends SimilarProductsHandler {

    protected boolean isInSimProduct;  // this attribute shall be true if the current tag is within the <sim_product> tags

    /**
     * {@inheritDoc}
     */
    public SimilarProductsHandlerLeipzig(Connection conn, String errorFilePath) throws IOException {
        super(conn, errorFilePath);
        isInSimProduct = false;
    }

    /**
     * Sets the product number of the current product to the asin value in this start tag.
     * @param attributes the attributes of the current start tag.
     */
    @Override
    protected void handleItemStartTag(Attributes attributes) {
        product_number = attributes.getValue("asin");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) {
        super.startElement(uri, localName, qName, attributes);
        if (qName.equals("sim_product")) {
            isInSimProduct = true;
        }
    }

    /**
     * {@inheritDoc}
     */
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

package com.unileipzig.shop;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

import java.sql.Connection;

public class ProductHandlerDresden extends ProductHandler {

    public ProductHandlerDresden(Connection conn) {
        super(conn);
    }

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        startElement(uri, localName, qName, attributes, false, false, false);
    }

    @Override
    public void readProductAttributes(String uri, String localName, String qName, Attributes attributes, boolean publisherIsAttribute, boolean labelIsAttribute) throws SAXException {
        super.readProductAttributes(uri, localName, qName, attributes, publisherIsAttribute, labelIsAttribute);
        if (qName.equals("details")) {
            if (!attributes.getValue("img").isBlank()) {
                product.setImage(attributes.getValue("img"));
            }
        }
    }

    @Override
    public void readProductTextElements(String uri, String localName, String qName) throws SAXException {
        super.readProductTextElements(uri, localName, qName);
        if (product instanceof Book && qName.equals("publisher")) {
            ((Book) product).addPublisher(currentValue.toString());
        } else if (product instanceof MusicCd && qName.equals("label")) {
            ((MusicCd) product).addLabel(currentValue.toString());
        }
    }
}

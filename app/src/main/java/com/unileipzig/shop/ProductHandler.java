package com.unileipzig.shop;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class ProductHandler extends DefaultHandler {

    private StringBuilder currentValue = new StringBuilder();

    @Override
    public void startDocument() throws SAXException {
        super.startDocument();

        System.out.println("Start Document");
    }

    @Override
    public void endDocument() throws SAXException {
        super.endDocument();

        System.out.println("End Document");
    }

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        super.startElement(uri, localName, qName, attributes);

        System.out.printf("Start Element : %s%n", qName);

        if (qName.equalsIgnoreCase("item")) {
            // get tag's attribute by name
            String pgroup = attributes.getValue("pgroup");
            String prod_number = attributes.getValue("asin");
            int sales_rank = Integer.parseInt(attributes.getValue("salesrank"));
            String image = attributes.getValue("picture");
        }
    }

    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
        super.endElement(uri, localName, qName);

        System.out.printf("End Element : %s%n", qName);
    }

    @Override
    public void characters(char[] ch, int start, int length) throws SAXException {
        super.characters(ch, start, length);

        currentValue.append(ch, start, length);
    }
}

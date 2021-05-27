package com.unileipzig.shop;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.sql.Connection;

public class ProductHandlerDresden extends ProductHandler {

    public ProductHandlerDresden(Connection conn, String errorPath) throws IOException {
        super(conn, errorPath);
    }

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        startElement(uri, localName, qName, attributes, false, false, false, false);
    }

    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
        super.endElement(uri, localName, qName);
        this.readPersonNamesAsTextElement(uri, localName, qName);
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

    public void readPersonNamesAsTextElement(String uri, String localName, String qName) {
        if (product instanceof MusicCd) {
            switch (qName) {
                case "label":
                    ((MusicCd) product).addLabel(currentValue.toString());
                    break;
                case "artist":
                    ((MusicCd) product).getArtists().add(new Person(currentValue.toString()));
                    break;
            }
        } else if (product instanceof Book) {
            switch (qName) {
                case "publisher":
                    ((Book) product).addPublisher(currentValue.toString());
                case "author":
                    ((Book) product).getAuthors().add(new Person(currentValue.toString()));
            }
        } else if (product instanceof Dvd) {
            switch (qName) {
                case "actor":
                    ((Dvd) product).getActors().add(new Person(currentValue.toString()));
                    break;
                case "creator":
                    ((Dvd) product).getCreators().add(new Person(currentValue.toString()));
                    break;
                case "director":
                    ((Dvd) product).getDirectors().add(new Person(currentValue.toString()));
                    break;
            }
        }
    }
}

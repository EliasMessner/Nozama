package com.unileipzig.shop;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;

public class ProductHandlerLeipzig extends ProductHandler {

    public ProductHandlerLeipzig(Connection conn, String errorPath) throws IOException {
        super(conn, errorPath);
    }

    @Override
    protected void readDvdAttributes(String qName, Attributes attributes) {
        switch (qName) {
            case "actor":
                ((Dvd) product).addActor(new Person(attributes.getValue("name")));
                break;
            case "creator":
                ((Dvd) product).addCreator(new Person(attributes.getValue("name")));
                break;
            case "director":
                ((Dvd) product).addDirector(new Person(attributes.getValue("name")));
                break;
        }
    }

    @Override
    protected void readMusicCdAttributes(String qName, Attributes attributes) {
        switch (qName) {
            case "label":
                if (!attributes.getValue("name").isBlank()) {
                    ((MusicCd) product).addLabel(attributes.getValue("name"));
                }
                break;
            case "artist":
                ((MusicCd) product).addArtist(new Person(attributes.getValue("name")));
                break;
        }
    }

    @Override
    protected void readBookAttributes(String qName, Attributes attributes) {
        super.readBookAttributes(qName, attributes);
        switch (qName) {
            case "publisher":
                if (!attributes.getValue("name").isBlank()) {
                    ((Book) product).addPublisher(attributes.getValue("name"));
                }
                break;
            case "author":
                ((Book) product).addAuthor(new Person(attributes.getValue("name")));
                break;
        }
    }

    @Override
    protected void startItemTag(Attributes attributes) {
        super.startItemTag(attributes);
        product.setImage(attributes.getValue("picture"));
    }
}

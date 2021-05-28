package com.unileipzig.shop;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.sql.Connection;
import java.time.LocalDate;

public class ProductHandlerDresden extends ProductHandler {

    public ProductHandlerDresden(Connection conn, String errorPath) throws IOException {
        super(conn, errorPath);
    }

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        super.startElement(uri, localName, qName, attributes);
        if (qName.equals("details")) {
            product.setImage(attributes.getValue("img"));
        }
    }
/*
    @Override
    protected void readProductTextElements(String uri, String localName, String qName) throws SAXException {
        if (currentValue.toString().isBlank()) return;
        super.readProductTextElements(uri, localName, qName);
        if (product instanceof Book) {
             readBookTextElements(qName);
        } else if (product instanceof MusicCd && qName.equals("label")) {
            ((MusicCd) product).addLabel(currentValue.toString());
        } else if (product instanceof Dvd) {
            ((Dvd) product).addCreator(new Person(currentValue.toString()));
        }
    }*/

    @Override
    protected void readBookTextElements(String qName) {
        super.readBookTextElements(qName);
        switch (qName) {
            case "author":
                ((Book) product).addAuthor(new Person(currentValue.toString()));
                break;
            case "publisher":
                ((Book) product).addPublisher(currentValue.toString());
                break;
        }
    }

    @Override
    protected void readMusicCdTextElements(String qName) {
        super.readMusicCdTextElements(qName);
        switch (qName) {
            case "artist":
                ((MusicCd) product).addArtist(new Person(currentValue.toString()));
                break;
            case "label":
                ((MusicCd) product).addLabel(currentValue.toString());
                break;
        }
    }

    @Override
    protected void readDvdTextElements(String qName) {
        super.readDvdTextElements(qName);
        switch (qName) {
            case "actor":
                ((Dvd) product).addActor(new Person(currentValue.toString()));
                break;
            case "creator":
                ((Dvd) product).addCreator(new Person(currentValue.toString()));
                break;
            case "director":
                ((Dvd) product).addDirector(new Person(currentValue.toString()));
                break;
        }
    }
}

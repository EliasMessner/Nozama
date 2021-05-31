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
        super.startElement(uri, localName, qName, attributes);
        if (qName.equals("details")) {
            product.setImage(attributes.getValue("img"));
        }
    }

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

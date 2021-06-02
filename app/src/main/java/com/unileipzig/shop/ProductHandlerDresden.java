package com.unileipzig.shop;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.sql.Connection;

/**
 * subclass for retrieving data from dresden.xml
 */
public class ProductHandlerDresden extends ProductHandler {

    /**
     *
     * @param conn
     * @param errorPath
     * @throws IOException
     */
    public ProductHandlerDresden(Connection conn, String errorPath) throws IOException {
        super(conn, errorPath);
    }

    /**
     *{@inheritDoc}
     */
    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        super.startElement(uri, localName, qName, attributes);
        if (qName.equals("details")) {
            product.setImage(attributes.getValue("img"));
        }
    }

    /**
     *{@inheritDoc}
     */
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

    /**
     *{@inheritDoc}
     */
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

    /**
     *{@inheritDoc}
     */
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

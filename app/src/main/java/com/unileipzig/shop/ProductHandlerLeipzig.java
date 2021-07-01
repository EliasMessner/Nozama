package com.unileipzig.shop;

import org.xml.sax.Attributes;
import java.io.IOException;
import java.sql.Connection;

/**
 * subclass for retrieving data from leipzig_transformed.xml
 */
public class ProductHandlerLeipzig extends ProductHandler {

    /**
     *
     * @param conn
     * @param errorPath
     * @throws IOException
     */
    public ProductHandlerLeipzig(Connection conn, String errorPath) throws IOException {
        super(conn, errorPath);
    }

    /**
     * {@inheritDoc}
     */
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

    /**
     * {@inheritDoc}
     */
    @Override
    protected void readMusicCdAttributes(String qName, Attributes attributes) {
        switch (qName) {
            case "label":
                if (this.attributeValueIsSpecified(attributes.getValue("name"))) {
                    ((MusicCd) product).addLabel(attributes.getValue("name"));
                }
                break;
            case "artist":
            case "author":
            case "creator":
                ((MusicCd) product).addArtist(new Person(attributes.getValue("name")));
                break;
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void readBookAttributes(String qName, Attributes attributes) {
        super.readBookAttributes(qName, attributes);
        switch (qName) {
            case "publisher":
                if (attributeValueIsSpecified(attributes.getValue("name"))) {
                    ((Book) product).addPublisher(attributes.getValue("name"));
                }
                break;
            case "author":
                ((Book) product).addAuthor(new Person(attributes.getValue("name")));
                break;
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void startItemTag(Attributes attributes) {
        super.startItemTag(attributes);
        if (attributeValueIsSpecified(attributes.getValue("picture")))
            product.setImage(attributes.getValue("picture"));
    }
}

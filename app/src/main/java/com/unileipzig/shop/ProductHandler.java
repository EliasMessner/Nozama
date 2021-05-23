package com.unileipzig.shop;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.math.BigInteger;
import java.time.LocalDate;

public class ProductHandler extends DefaultHandler {

    private StringBuilder currentValue = new StringBuilder();
    private Product product;

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

        if (qName.equals("item")) {
            switch (attributes.getValue("pgroup")){
                case "Music":
                    product = new MusicCd(Integer.parseInt(attributes.getValue("asin")), null);
                    break;
                case "Book":
                    product = new Book(Integer.parseInt(attributes.getValue("asin")), null);
                    break;
                case "DVD":
                    product = new Dvd(Integer.parseInt(attributes.getValue("asin")), null);
                    break;
                default:
                    throw new SAXException(new XmlException("Incorrect XML format. Specified product type does not exist!"));
            }

            product.setSalesRank(Integer.parseInt(attributes.getValue("salesrank")));
            product.setImage(attributes.getValue("picture"));
        }

        this.readProductAttributes(uri, localName, qName, attributes);
    }

    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
        super.endElement(uri, localName, qName);

        System.out.printf("End Element : %s%n", qName);

        if (qName.equals("item")) {
            // save product to database
        }

        this.readProductTextElements(uri, localName, qName);
    }

    @Override
    public void characters(char[] ch, int start, int length) throws SAXException {
        super.characters(ch, start, length);

        currentValue.append(ch, start, length);
    }

    public void readProductAttributes(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        if (product instanceof MusicCd){
            switch (qName) {
                case "label":
                    ((MusicCd) product).getLabels().add(attributes.getValue("name"));
                    break;
            }
        } else if (product instanceof Book){
            switch(qName) {
                case "publication":
                    ((Book) product).setPublicationDate(LocalDate.parse(attributes.getValue("date")));
                    break;
                case "isbn":
                    ((Book) product).setIsbn(new BigInteger(attributes.getValue("val")));
                    break;
                case "publisher":
                    ((Book) product).getPublishers().add(attributes.getValue("name"));
                    break;
            }
        }

    }

    public void readProductTextElements(String uri, String localName, String qName) throws SAXException {
        if (qName.equals("title")) {
            product.setTitle(currentValue.toString());
            return;
        }

        if (product instanceof MusicCd) {
            switch (qName) {
                case "releaseDate":
                    ((MusicCd) product).setPublicationDate(LocalDate.parse(currentValue.toString()));
                    break;
                case "title":
                    ((MusicCd) product).getTitles().add(currentValue.toString());
                    break;
            }
        } else if (product instanceof Book) {
            switch (qName) {
                case "pages":
                    ((Book) product).setPageNumber(Integer.parseInt(currentValue.toString()));
                    break;
                case "releaseDate":
                    ((MusicCd) product).setPublicationDate(LocalDate.parse(currentValue.toString()));
                    break;
            }
        } else if (product instanceof Dvd) {
            switch (qName) {
                case "format":
                    ((Dvd) product).setFormat(currentValue.toString());
                    break;
                case "runningtime":
                    ((Dvd) product).setDurationMinutes(Integer.parseInt(currentValue.toString()));
                    break;
                case "regioncode":
                    ((Dvd) product).setRegionCode(Short.parseShort(currentValue.toString()));
                    break;
            }
        }
    }
}

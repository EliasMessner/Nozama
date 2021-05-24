package com.unileipzig.shop;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.*;
import java.time.LocalDate;
import java.util.Arrays;

public class ProductHandler extends DefaultHandler {

    private StringBuilder currentValue = new StringBuilder();
    private Product product;
    private boolean tracks = false;
    private PrintWriter printWriter;

    @Override
    public void startDocument() throws SAXException {
        super.startDocument();

        System.out.println("Start Document");

        FileWriter fileWriter = null;
        try {
            fileWriter = new FileWriter("/data/errors.txt");
            printWriter = new PrintWriter(fileWriter);
        } catch (IOException e) {
            throw new SAXException(e);
        }
    }

    @Override
    public void endDocument() throws SAXException {
        super.endDocument();

        System.out.println("End Document");

        printWriter.close();
    }

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        super.startElement(uri, localName, qName, attributes);

        currentValue.setLength(0);

        System.out.printf("Start Element : %s%n", qName);

        if (qName.equals("item")) {
            System.out.println("Current item: " + attributes.getValue("asin"));

            switch (attributes.getValue("pgroup")){
                case "Music":
                    product = new MusicCd(attributes.getValue("asin"), null);
                    break;
                case "Book":
                    product = new Book(attributes.getValue("asin"), null);
                    break;
                case "DVD":
                    product = new Dvd(attributes.getValue("asin"), null);
                    break;
                default:
                    throw new SAXException(new XmlException("Incorrect XML format. Specified product type does not exist!"));
            }

            if (!attributes.getValue("salesrank").isBlank()) {
                product.setSalesRank(Integer.parseInt(attributes.getValue("salesrank")));
            }

            product.setImage(attributes.getValue("picture"));
        } else if (qName.equals("tracks")) {
            tracks = true;
        }

        this.readProductAttributes(uri, localName, qName, attributes);
    }

    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
        super.endElement(uri, localName, qName);

        System.out.printf("End Element : %s%n", qName);

        if (qName.equals("item")) {
            try {
                this.persistProduct();
            } catch (SQLException throwables) {
                printWriter.println(throwables.getMessage());
                printWriter.println(Arrays.toString(throwables.getStackTrace()));
            }
        } else if (qName.equals("tracks")) {
            tracks = false;
        }

        if (currentValue.toString().isBlank()) {
            return;
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
                    if (!attributes.getValue("name").isBlank()) {
                        ((MusicCd) product).getLabels().add(attributes.getValue("name"));
                    }
                    break;
            }
        } else if (product instanceof Book){
            switch(qName) {
                case "publication":
                    if (!attributes.getValue("date").isBlank()) {
                        ((Book) product).setPublicationDate(LocalDate.parse(attributes.getValue("date")));
                    }
                    break;
                case "isbn":
                    if (!attributes.getValue("val").isBlank()) {
                        ((Book) product).setIsbn(attributes.getValue("val"));
                    }
                    break;
                case "publisher":
                    if (!attributes.getValue("name").isBlank()) {
                        ((Book) product).getPublishers().add(attributes.getValue("name"));
                    }
                    break;
            }
        }

    }

    public void readProductTextElements(String uri, String localName, String qName) throws SAXException {
        if (qName.equals("title") && !tracks) {
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

    public void persistProduct() throws SQLException {
        Connection conn = DatabaseConnector.getConnection();

        PreparedStatement pStmt0 = conn.prepareStatement("INSERT INTO product (prod_number, title, rating, " +
                "sales_rank, image) VALUES (?, ?, 2.5, ?, ?)");
        pStmt0.setString(1, product.getProdNumber());
        pStmt0.setString(2, product.getTitle());
        pStmt0.setInt(3, product.getSalesRank());
        pStmt0.setString(4, product.getImage());
        pStmt0.executeUpdate();

        if (product instanceof MusicCd) {
            PreparedStatement pStmt = conn.prepareStatement("INSERT INTO music_cd (prod_number, labels, " +
                    "publication_date, titles) VALUES (?, ?, ?, ?)");
            pStmt.setString(1, product.getProdNumber());
            pStmt.setArray(2, conn.createArrayOf("VARCHAR", ((MusicCd) product).getLabels().toArray()));
            if (((MusicCd) product).getPublicationDate() != null) {
                pStmt.setDate(3, Date.valueOf(((MusicCd) product).getPublicationDate()));
            } else {
                pStmt.setNull(3, Types.DATE);
            }
            pStmt.setArray(4, conn.createArrayOf("VARCHAR", ((MusicCd) product).getTitles().toArray()));
            pStmt.executeUpdate();
        }
    }
}

package com.unileipzig.shop;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import javax.xml.crypto.Data;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class ProductHandler extends DefaultHandler {

    protected StringBuilder currentValue = new StringBuilder();
    protected Product product;
    protected boolean tracks = false;
    protected boolean similars = false;
    protected PrintWriter printWriter;
    protected Shop shop;
    protected Connection conn;

    public ProductHandler(Connection conn, String errorPath) throws IOException {
        super();
        this.conn = conn;

        FileWriter fileWriter = null;
        try {
            fileWriter = new FileWriter(errorPath);
            printWriter = new PrintWriter(fileWriter);
        } catch (IOException e) {
            throw new IOException(e);
        }
    }

    @Override
    public void startDocument() throws SAXException {
        super.startDocument();

        System.out.println("Start Document");
    }

    @Override
    public void endDocument() throws SAXException {
        super.endDocument();

        System.out.println("End Document");

        printWriter.close();
    }

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        startElement(uri, localName, qName, attributes, true, true, true);
    }

    public void startElement(String uri, String localName, String qName, Attributes attributes, boolean imgIsAttribute, boolean publisherIsAttribute, boolean labelIsAttribute) throws SAXException {
        super.startElement(uri, localName, qName, attributes);

        currentValue.setLength(0);

        //System.out.printf("Start Element : %s%n", qName);
        if (qName.equals("shop")) {
            shop = new Shop(attributes.getValue("name"),
                    attributes.getValue("street"),
                    Integer.parseInt(attributes.getValue("zip")));
        }
        if (qName.equals("item") && !similars) {
            System.out.println("Current item: " + attributes.getValue("asin"));

            switch (attributes.getValue("pgroup")){
                case "Music":
                    product = new MusicCd(attributes.getValue("asin"), null);
                    break;
                case "Book":
                case "Buch":
                    product = new Book(attributes.getValue("asin"), null);
                    break;
                case "DVD":
                    product = new Dvd(attributes.getValue("asin"), null);
                    break;
                default:
                    printWriter.println("ERROR: product type " + attributes.getValue("pgroup") + " does not exist.");
            }

            if (!attributes.getValue("salesrank").isBlank()) {
                product.setSalesRank(Integer.parseInt(attributes.getValue("salesrank")));
            }

            if (imgIsAttribute) {
                product.setImage(attributes.getValue("picture"));
            }
        } else if (qName.equals("tracks")) {
            tracks = true;
        } else if (qName.equals("similars")) {
            similars = true;
        }

        this.readProductAttributes(uri, localName, qName, attributes, publisherIsAttribute, labelIsAttribute);
    }

    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
        super.endElement(uri, localName, qName);

        //System.out.printf("End Element : %s%n", qName);

        if (qName.equals("shop")) {
            try {
                persistShop();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        } else if (qName.equals("item") && !similars) {
            try {
                this.persistProduct();
                this.persistPersonAndRelations();
            } catch (SQLException throwables) {
                printWriter.println(throwables.getMessage());
                throwables.printStackTrace();
            }
        } else if (qName.equals("tracks")) {
            tracks = false;
        } else if (qName.equals("similars")) {
            similars = false;
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

    public void readProductAttributes(String uri, String localName, String qName, Attributes attributes, boolean publisherIsAttribute, boolean labelIsAttribute) throws SAXException {
        if (product instanceof MusicCd){
            switch (qName) {
                case "label":
                    if (labelIsAttribute && !attributes.getValue("name").isBlank()) {
                        ((MusicCd) product).getLabels().add(attributes.getValue("name"));
                    }
                    break;
                case "artist":
                    ((MusicCd) product).getArtists().add(new Person(attributes.getValue("name")));
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
                    if (publisherIsAttribute && !attributes.getValue("name").isBlank()) {
                        ((Book) product).getPublishers().add(attributes.getValue("name"));
                    }
                    break;
                case "author":
                    ((Book) product).getAuthors().add(new Person(attributes.getValue("name")));
                    break;
            }
        }
    }

    public void readProductTextElements(String uri, String localName, String qName) throws SAXException {
        if (qName.equals("title") && !tracks && !similars) {
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

    public void persistShop() throws SQLException {
        PreparedStatement pStmt = conn.prepareStatement("INSERT INTO store (s_name, street, zip) VALUES (?, ?, ?)" +
                "ON CONFLICT (s_name, street, zip) DO NOTHING");
        pStmt.setString(1, shop.getName());
        pStmt.setString(2, shop.getStreet());
        pStmt.setInt(3, shop.getZip());
        pStmt.executeUpdate();
    }

    public void persistProduct() throws SQLException {
        PreparedStatement pStmt0 = conn.prepareStatement("INSERT INTO product (prod_number, title, rating, " +
                "sales_rank, image) VALUES (?, ?, 2.5, ?, ?)");
        pStmt0.setString(1, product.getProdNumber());
        pStmt0.setString(2, product.getTitle());
        pStmt0.setInt(3, product.getSalesRank());
        pStmt0.setString(4, product.getImage());
        pStmt0.executeUpdate();

        if (product instanceof MusicCd) {
            this.persistMusicCd();
        } else if (product instanceof Book) {
            this.persistBook();
        } else if (product instanceof Dvd){
            PreparedStatement pStmt3 = conn.prepareStatement("INSERT INTO dvd (prod_number, format, " +
                    "duration_minutes, region_code) VALUES (?, ?, ?, ?)");
            pStmt3.setString(1, product.getProdNumber());
            pStmt3.setString(2, ((Dvd) product).getFormat());
            pStmt3.setInt(3, ((Dvd) product).getDurationMinutes());
            pStmt3.setShort(4, ((Dvd) product).getRegionCode());
            pStmt3.executeUpdate();
        }
    }

    public void persistBook() throws SQLException {
        PreparedStatement pStmt = conn.prepareStatement("INSERT INTO book (prod_number, page_number, " +
                "publication_date, isbn, publishers) VALUES (?, ?, ?, ?, ?)");
        pStmt.setString(1, product.getProdNumber());
        pStmt.setInt(2, ((Book) product).getPageNumber());
        if (((Book) product).getPublicationDate() != null) {
            pStmt.setDate(3, Date.valueOf(((Book) product).getPublicationDate()));
        } else {
            pStmt.setNull(3, Types.DATE);
        }
        pStmt.setString(4, ((Book) product).getIsbn());
        pStmt.setArray(5, conn.createArrayOf("VARCHAR", ((Book) product).getPublishers().toArray()));
        pStmt.executeUpdate();
    }

    public void persistMusicCd() throws SQLException {
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

    public void persistPersonAndRelations() throws SQLException {
        String productPersonTableName;
        String columnNames;
        List<Person> personList = new ArrayList<>();

        if (product instanceof MusicCd) {
            productPersonTableName = "cd_artist";
            columnNames = "(cd, artist)";
            personList = ((MusicCd) product).getArtists();
        } else if (product instanceof Book) {
            productPersonTableName = "book_author";
            columnNames = "(book, author)";
            personList = ((Book) product).getAuthors();
        } else {
            return;
        }

        PreparedStatement pStmt2 = conn.prepareStatement("SELECT id FROM person WHERE name = ?");
        PreparedStatement pStmt3 = conn.prepareStatement("INSERT INTO person (name) VALUES (?)",
                Statement.RETURN_GENERATED_KEYS);
        PreparedStatement pStmtRelation = conn.prepareStatement("INSERT INTO " + productPersonTableName + " " +
                columnNames + " VALUES (?, ?)");
        pStmtRelation.setString(1, product.getProdNumber());

        for (Person person: personList) {
            pStmt2.setString(1, person.getName());
            ResultSet personIds = pStmt2.executeQuery();

            int personId;
            if (personIds.next()) {
                personId = personIds.getInt(1);
            } else {
                pStmt3.setString(1, person.getName());
                pStmt3.executeUpdate();
                ResultSet generatedKeys = pStmt3.getGeneratedKeys();
                generatedKeys.next();
                personId = generatedKeys.getInt(1);
            }

            pStmtRelation.setInt(2, personId);
            pStmtRelation.executeUpdate();
        }
    }
}

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
                conn.setAutoCommit(false);
                this.persistProduct();
                this.persistPersonAndRelations();
                conn.commit();
            } catch (SQLException throwables) {
                try {
                    conn.rollback();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                printWriter.println(throwables.getMessage());
            } finally {
                try {
                    conn.setAutoCommit(true);
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
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
            switch (qName) {
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
        } else if (product instanceof Dvd) {
            switch (qName) {
                case "actor":
                    ((Dvd) product).getActors().add(new Person(attributes.getValue("name")));
                    break;
                case "creator":
                    ((Dvd) product).getCreators().add(new Person(attributes.getValue("name")));
                    break;
                case "director":
                    ((Dvd) product).getDirectors().add(new Person(attributes.getValue("name")));
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
        if (!productExists()) {
            PreparedStatement pStmt0 = conn.prepareStatement("INSERT INTO product (prod_number, title, rating, " +
                    "sales_rank, image) VALUES (?, ?, 3, ?, ?)");
            pStmt0.setString(1, product.getProdNumber());
            pStmt0.setString(2, product.getTitle());
            pStmt0.setInt(3, product.getSalesRank());
            pStmt0.setString(4, product.getImage());
            pStmt0.executeUpdate();
        }
        if (product instanceof MusicCd && !musicCdExists()) {
            this.persistMusicCd();
        } else if (product instanceof Book && !bookExists()) {
            this.persistBook();
        } else if (product instanceof Dvd && !dvdExists()) {
            this.persistDvd();
        }
    }

    private boolean productExists() throws SQLException {
        PreparedStatement pStmt = conn.prepareStatement("SELECT FROM product WHERE prod_number = ?");
        pStmt.setString(1, product.getProdNumber());
        ResultSet resultSet = pStmt.executeQuery();
        Product other = getProductFromResultSet(resultSet);
        return product.equals(other);
    }

    private Product getProductFromResultSet(ResultSet resultSet) throws SQLException {
        if (resultSet.next()) {
            Product product = new Product(resultSet.getString("prod_number"),
                    resultSet.getString("title"));
            product.setRating(resultSet.getDouble("rating"));
            product.setSalesRank(resultSet.getInt("sales_rank"));
            product.setImage(resultSet.getString("image"));
            return product;
        }
        return null;
    }

    private boolean dvdExists() {
        return false; //TODO
    }

    private boolean bookExists() {
        return false; //TODO
    }

    private boolean musicCdExists() {
        return false; //TODO
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

    public void persistDvd() throws SQLException {
        PreparedStatement pStmt = conn.prepareStatement("INSERT INTO dvd (prod_number, format, " +
                "duration_minutes, region_code) VALUES (?, ?, ?, ?)");
        pStmt.setString(1, product.getProdNumber());
        pStmt.setString(2, ((Dvd) product).getFormat());
        pStmt.setInt(3, ((Dvd) product).getDurationMinutes());
        pStmt.setShort(4, ((Dvd) product).getRegionCode());
        pStmt.executeUpdate();
    }

    public void persistPersonAndRelations() throws SQLException {
        if (product instanceof MusicCd) {
            this.persistPersonAndRelationsHelper("artist", ((MusicCd) product).getArtists());
        } else if (product instanceof Book) {
            this.persistPersonAndRelationsHelper("author", ((Book) product).getAuthors());
        } else if (product instanceof Dvd){
            this.persistPersonAndRelationsHelper("actor", ((Dvd) product).getActors());
            this.persistPersonAndRelationsHelper("creator", ((Dvd) product).getCreators());
            this.persistPersonAndRelationsHelper("director", ((Dvd) product).getDirectors());
        }
    }

    private void persistPersonAndRelationsHelper(String role, List<Person> personList) throws SQLException {
        PreparedStatement pStmt2 = conn.prepareStatement("SELECT id FROM person WHERE name = ?");
        PreparedStatement pStmt3 = conn.prepareStatement("INSERT INTO person (name) VALUES (?)",
                Statement.RETURN_GENERATED_KEYS);
        PreparedStatement pStmtRelation;

        switch(role) {
            case "artist":
                pStmtRelation = conn.prepareStatement("INSERT INTO cd_artist (cd, artist) " +
                        "VALUES (?, ?)");
                break;
            case "author":
                pStmtRelation = conn.prepareStatement("INSERT INTO book_author (book, author) " +
                        "VALUES (?, ?)");
                break;
            case "actor":
                pStmtRelation = conn.prepareStatement("INSERT INTO dvd_person (dvd, person, role) " +
                        "VALUES (?, ?, 'actor')");
                break;
            case "creator":
                pStmtRelation = conn.prepareStatement("INSERT INTO dvd_person (dvd, person, role) " +
                        "VALUES (?, ?, 'creator')");
                break;
            case "director":
                pStmtRelation = conn.prepareStatement("INSERT INTO dvd_person (dvd, person, role) " +
                        "VALUES (?, ?, 'director')");
                break;
            default:
                conn.rollback();
                return;
        }

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

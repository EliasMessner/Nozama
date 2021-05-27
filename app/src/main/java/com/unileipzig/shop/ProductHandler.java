package com.unileipzig.shop;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
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
        try {
            if (qName.equals("shop")) {
                persistShop();
            } else if (qName.equals("item") && !similars) {
                try {
                    conn.setAutoCommit(false);
                    this.persistProduct();
                    this.persistPersonAndRelations();
                    conn.commit();
                } catch (SQLException throwables) {
                    conn.rollback();
                    throwables.printStackTrace();
                    printWriter.println(throwables.getMessage());
                } finally {
                    conn.setAutoCommit(true);
                }
            } else if (qName.equals("tracks")) {
                tracks = false;
            } else if (qName.equals("similars")) {
                similars = false;
            }
        } catch (SQLException e) {
            e.printStackTrace();
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
                        ((MusicCd) product).addLabel(attributes.getValue("name"));
                    }
                    break;
                case "artist":
                    ((MusicCd) product).addArtist(new Person(attributes.getValue("name")));
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
                        ((Book) product).addPublisher(attributes.getValue("name"));
                    }
                    break;
                case "author":
                    ((Book) product).addAuthor(new Person(attributes.getValue("name")));
                    break;
            }
        } else if (product instanceof Dvd) {
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
    }

    public void readProductTextElements(String uri, String localName, String qName) throws SAXException {
        if (qName.equals("title") && !tracks && !similars) {
            product.setTitle(currentValue.toString());
            return;
        }
        if (product instanceof MusicCd) {
            switch (qName) {
                case "releasedate":
                    ((MusicCd) product).setPublicationDate(LocalDate.parse(currentValue.toString()));
                    break;
                case "title":
                    ((MusicCd) product).addTitle(currentValue.toString());
                    break;
            }
        } else if (product instanceof Book) {
            switch (qName) {
                case "pages":
                    ((Book) product).setPageNumber(Integer.parseInt(currentValue.toString()));
                    break;
                case "releasedate":
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
        PreparedStatement pStmt = conn.prepareStatement("INSERT INTO store (s_name, street, zip) VALUES (?, ?, ?) " +
                "ON CONFLICT (s_name, street, zip) DO NOTHING");
        pStmt.setString(1, shop.getName());
        pStmt.setString(2, shop.getStreet());
        pStmt.setInt(3, shop.getZip());
        pStmt.executeUpdate();
    }

    public void persistProduct() throws SQLException {
        if (!productExists()) { //TODO on conflict...
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
        PreparedStatement pStmt = conn.prepareStatement("SELECT * FROM product WHERE prod_number = ?");
        pStmt.setString(1, product.getProdNumber());
        ResultSet resultSet = pStmt.executeQuery();
        Product other = getProductFromResultSet(resultSet);
        return product.equals(other); //TODO merge
    }

    private Product getProductFromResultSet(ResultSet resultSet) throws SQLException {
        if (resultSet.next()) {
            Product parsedProduct = new Product(resultSet.getString("prod_number"),
                    resultSet.getString("title"));
            parsedProduct.setRating(resultSet.getDouble("rating"));
            parsedProduct.setSalesRank(resultSet.getInt("sales_rank"));
            parsedProduct.setImage(resultSet.getString("image"));
            return parsedProduct;
        }
        return null;
    }

    private boolean dvdExists() throws SQLException {
        PreparedStatement pStmt = conn.prepareStatement("SELECT * FROM dvd NATURAL JOIN product" +
                " WHERE prod_number = ?");
        pStmt.setString(1, product.getProdNumber());
        ResultSet resultSet = pStmt.executeQuery();
        Dvd other = getDvdFromResultSet(resultSet);
        return product.equals(other);
    }

    private Dvd getDvdFromResultSet(ResultSet resultSet) throws SQLException {
        if (resultSet.next()) {
            Dvd dvd = new Dvd(resultSet.getString("prod_number"),
                    resultSet.getString("title"));
            dvd.setRating(resultSet.getDouble("rating"));
            dvd.setSalesRank(resultSet.getInt("sales_rank"));
            dvd.setImage(resultSet.getString("image"));
            dvd.setFormat(resultSet.getString("format"));
            dvd.setDurationMinutes(resultSet.getInt("duration_minutes"));
            dvd.setRegionCode(resultSet.getShort("region_code"));
            obtainPersonRelations(dvd);
            return dvd;
        }
        return null;
    }

    private boolean bookExists() throws SQLException {
        PreparedStatement pStmt = conn.prepareStatement("SELECT * FROM book NATURAL JOIN product" +
                " WHERE prod_number = ?");
        pStmt.setString(1, product.getProdNumber());
        ResultSet resultSet = pStmt.executeQuery();
        Book other = getBookFromResultSet(resultSet);
        return product.equals(other);
    }

    private Book getBookFromResultSet(ResultSet resultSet) throws SQLException {
        if (resultSet.next()) {
            Book book = new Book(resultSet.getString("prod_number"),
                    resultSet.getString("title"));
            book.setRating(resultSet.getDouble("rating"));
            book.setSalesRank(resultSet.getInt("sales_rank"));
            book.setImage(resultSet.getString("image"));
            book.setPageNumber(resultSet.getInt("page_number"));
            book.setPublicationDate(resultSet.getDate("publication_date").toLocalDate());
            obtainPersonRelations(book);
            return book;
        }
        return null;
    }

    private boolean musicCdExists() throws SQLException {
        PreparedStatement pStmt = conn.prepareStatement("SELECT * FROM music_cd NATURAL JOIN product" +
                " WHERE prod_number = ?");
        pStmt.setString(1, product.getProdNumber());
        ResultSet resultSet = pStmt.executeQuery();
        MusicCd other = getMusicCdFromResultSet(resultSet);
        return product.equals(other);
    }

    private MusicCd getMusicCdFromResultSet(ResultSet resultSet) throws SQLException {
        if (resultSet.next()) {
            MusicCd musicCd = new MusicCd(resultSet.getString("prod_number"),
                    resultSet.getString("title"));
            musicCd.setRating(resultSet.getDouble("rating"));
            musicCd.setSalesRank(resultSet.getInt("sales_rank"));
            musicCd.setImage(resultSet.getString("image"));
            String[] labels = (String[]) resultSet.getArray("labels").getArray();
            musicCd.setLabels(Arrays.asList(labels));
            musicCd.setPublicationDate(resultSet.getDate("publication_date") != null ? resultSet.getDate("publication_date").toLocalDate() : null);
            String[] titles = (String[]) resultSet.getArray("titles").getArray();
            musicCd.setTitles(Arrays.asList(titles));
            obtainPersonRelations(musicCd);
            return musicCd;
        }
        return null;
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

    private void obtainPersonRelations(Product p) throws SQLException {
        if (p instanceof MusicCd) {
            ((MusicCd) p).setArtists(getPersonListForRelation(p, "artist"));
        } else if (p instanceof Book) {
            ((Book) p).setAuthors(getPersonListForRelation(p, "author"));
        } else if (p instanceof Dvd){
            ((Dvd) p).setActors(getPersonListForRelation(p, "actor"));
            ((Dvd) p).setCreators(getPersonListForRelation(p, "creator"));
            ((Dvd) p).setDirectors(getPersonListForRelation(p, "director"));
        }
    }

    private List<Person> getPersonListForRelation(Product p, String role) throws SQLException {
        PreparedStatement pStmt;
        String col;
        switch (role) {
            case "artist":
                pStmt = conn.prepareStatement("SELECT artist FROM cd_artist WHERE cd = ?");
                col = "artist";
                break;
            case "author":
                pStmt = conn.prepareStatement("SELECT author FROM book_author WHERE book = ?");
                col = "author";
                break;
            case "actor":
                pStmt = conn.prepareStatement("SELECT person FROM dvd_person WHERE dvd = ? " +
                        "AND role = 'actor'");
                col = "person";
                break;
            case "creator":
                pStmt = conn.prepareStatement("SELECT person FROM dvd_person WHERE dvd = ? " +
                        "AND role = 'creator'");
                col = "person";
                break;
            case "director":
                pStmt = conn.prepareStatement("SELECT person FROM dvd_person WHERE dvd = ? " +
                        "AND role = 'director'");
                col = "person";
                break;
            default:
                conn.rollback();
                return null;
        }
        pStmt.setString(1, p.getProdNumber());
        ResultSet resultSet = pStmt.executeQuery();
        List<Integer> personIDs = columnToIntegerList(resultSet, col);
        return getPersonsFromIDs(personIDs);
    }

    private List<Person> getPersonsFromIDs(List<Integer> personIDs) throws SQLException {
        List<Person> result = new ArrayList<>();
        PreparedStatement pStmt = conn.prepareStatement("SELECT * FROM person WHERE id = ?");
        for (int id : personIDs) {
            pStmt.setInt(1, id);
            ResultSet rs = pStmt.executeQuery();
            if (rs.next()) {
                result.add(new Person(id, rs.getString("name")));
            }
        }
        return result;
    }

    private List<Integer> columnToIntegerList(ResultSet resultSet, String col) throws SQLException {
        List<Integer> result = new ArrayList<>();
        while (resultSet.next()) {
            result.add(resultSet.getInt(col));
        }
        return result;
    }
}

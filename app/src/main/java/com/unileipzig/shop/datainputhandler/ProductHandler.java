package com.unileipzig.shop.datainputhandler;

import com.unileipzig.shop.model.*;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.*;
import java.sql.Date;
import java.time.LocalDate;
import java.util.*;

/**
 * class for retrieving data from dresden.xml and leipzig_transformed.xml
 */
public abstract class ProductHandler extends DefaultHandler {

    protected StringBuilder currentValue = new StringBuilder();
    protected Product product;
    protected Offer currentOffer;
    protected List<Offer> offers = new ArrayList<>();
    protected boolean tracks = false;
    protected boolean similars = false;
    protected PrintWriter printWriter;
    protected Shop shop;
    protected Connection conn;

    public ProductHandler(Connection conn, String errorPath) throws IOException {
        super();
        this.conn = conn;
        FileWriter fileWriter;
        try {
            fileWriter = new FileWriter(errorPath);
            printWriter = new PrintWriter(fileWriter);
        } catch (IOException e) {
            throw new IOException(e);
        }
    }

    /**
     *{@inheritDoc}
     */
    @Override
    public void startDocument() throws SAXException {
        System.out.println("Start Document");
    }

    /**
     *{@inheritDoc}
     */
    @Override
    public void endDocument() throws SAXException {
        System.out.println("End Document");
        printWriter.close();
    }

    /**
     *{@inheritDoc}
     */
    @Override
    public void characters(char[] ch, int start, int length) throws SAXException {
        super.characters(ch, start, length);
        currentValue.append(ch, start, length);
    }

    /**
     *{@inheritDoc}
     */
    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        currentValue.setLength(0);
        switch (qName) {
            case "shop":
                parseShop(attributes);
                persistShop();
                break;
            case "item":
                startItemTag(attributes);
                break;
            case "tracks":
                tracks = true;
                break;
            case "similars":
                similars = true;
                break;
            case "price":
                currentOffer = new Offer(product, shop);
                offers.add(currentOffer);
                if (this.attributeValueIsSpecified(attributes.getValue("state"))) {
                    currentOffer.setArticleCondition(attributes.getValue("state"));
                }
                break;
        }
        readProductAttributes(qName, attributes);
    }

    /**
     *{@inheritDoc}
     */
    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
        try {
            switch (qName) {
                case "item":
                    endItemTag();
                    break;
                case "tracks":
                    tracks = false;
                    break;
                case "similars":
                    similars = false;
                    break;
                case "price":
                    currentOffer.setPrice(parsePrice());
            }
        } catch (SQLException e) {
            printWriter.println(e.getMessage());
            e.printStackTrace();
        }
        this.readProductTextElements(qName);
    }

    /**
     * computes price from xml values
     * @return the computed price
     */
    protected BigDecimal parsePrice() {
        if (currentValue.toString().isBlank())
            return null;
        BigDecimal price100 = new BigDecimal(currentValue.toString());
        return price100.divide(new BigDecimal("100"), RoundingMode.HALF_UP);
    }

    /**
     * creates shop from xml values
     * @param attributes
     */
    protected void parseShop(Attributes attributes) {
        shop = new Shop(attributes.getValue("name"),
                attributes.getValue("street"),
                attributes.getValue("zip"));
    }

    /**
     * chooses and creates correct product type and creates corresponding offer
     * @param attributes
     */
    protected void startItemTag(Attributes attributes) {
        if (similars) return;
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
        if (attributeValueIsSpecified(attributes.getValue("salesrank"))) {
            product.setSalesRank(Integer.parseInt(attributes.getValue("salesrank")));
        }
    }

    /**
     * persist product, person (with relations) and offer
     * @throws SQLException
     */
    protected void endItemTag() throws SQLException {
        if (similars) return;
        try {
            conn.setAutoCommit(false);
            this.persistProduct();
            this.persistPersonAndRelations();
            conn.commit(); // commit here already because if only persistOffer fails we still want to keep the product
            this.persistOffers();
            conn.commit();
        } catch (SQLException throwables) {
            conn.rollback();
            throwables.printStackTrace();
            printWriter.println(throwables.getMessage());
        } finally {
            conn.setAutoCommit(true);
        }
        offers = new ArrayList<>();
        currentOffer = null;
    }

    protected void readProductAttributes(String qName, Attributes attributes) {
        if (product instanceof MusicCd){
            readMusicCdAttributes(qName, attributes);
        } else if (product instanceof Book){
            readBookAttributes(qName, attributes);
        } else if (product instanceof Dvd) {
            readDvdAttributes(qName, attributes);
        }
    }

    protected void readDvdAttributes(String qName, Attributes attributes) {

    }

    protected void readBookAttributes(String qName, Attributes attributes) {
        switch (qName) {
            case "publication":
                if (this.attributeValueIsSpecified(attributes.getValue("date")))
                    ((Book) product).setPublicationDate(LocalDate.parse(attributes.getValue("date")));
                break;
            case "isbn":
                if (this.attributeValueIsSpecified(attributes.getValue("val")))
                    ((Book) product).setIsbn(attributes.getValue("val"));
                break;
        }
    }

    protected void readMusicCdAttributes(String qName, Attributes attributes) {

    }

    protected void readProductTextElements(String qName) {
        if (!textElementIsSpecified(currentValue.toString())) return;
        if (qName.equals("title") && !tracks && !similars) {
            product.setTitle(currentValue.toString());
            return;
        }
        if (product instanceof MusicCd) {
            readMusicCdTextElements(qName);
        } else if (product instanceof Book) {
            readBookTextElements(qName);
        } else if (product instanceof Dvd) {
            readDvdTextElements(qName);
        }
    }

    protected void readDvdTextElements(String qName) {
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

    protected void readBookTextElements(String qName) {
        if ("pages".equals(qName)) {
            ((Book) product).setPageNumber(Integer.parseInt(currentValue.toString()));
        }
    }

    protected void readMusicCdTextElements(String qName) {
        switch (qName) {
            case "releasedate":
                ((MusicCd) product).setPublicationDate(currentValue.toString().isBlank() ? null : LocalDate.parse(currentValue.toString()));
                break;
            case "title":
                if (tracks) ((MusicCd) product).addTitle(currentValue.toString());
                break;
        }
    }

    private void persistShop(){
        try {
            PreparedStatement pStmt = conn.prepareStatement("INSERT INTO store (s_name, street, zip) VALUES (?, ?, ?) " +
                    "ON CONFLICT (s_name, street, zip) DO NOTHING");
            pStmt.setString(1, shop.getName());
            pStmt.setString(2, shop.getStreet());
            pStmt.setString(3, shop.getZip());
            pStmt.executeUpdate();
        } catch (SQLException e) {
            printWriter.println(e.getMessage());
            e.printStackTrace();
        }
    }

    private void persistProduct() throws SQLException {
        if (!productExists()) {
            PreparedStatement pStmt0 = conn.prepareStatement("INSERT INTO product (prod_number, title, " +
                    "sales_rank, image) VALUES (?, ?, ?, ?)");
            pStmt0.setString(1, product.getProdNumber());
            pStmt0.setString(2, product.getTitle());
            pStmt0.setObject(3, product.getSalesRank(), Types.INTEGER);
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

    private void persistOffers() throws SQLException {
        for (Offer offer : offers) {
            persistOffer(offer);
        }
    }

    private void persistOffer(Offer offer) throws SQLException {
        PreparedStatement pStmt = conn.prepareStatement("INSERT INTO store_inventory (product, " +
                "store_name, store_street, store_zip, article_condition, price) VALUES (?, ?, ?, ?, ?, ?)");
        pStmt.setString(1, offer.getProduct().getProdNumber());
        pStmt.setString(2, offer.getShop().getName());
        pStmt.setString(3, offer.getShop().getStreet());
        pStmt.setString(4, offer.getShop().getZip());
        pStmt.setString(5, offer.getArticleCondition());
        pStmt.setBigDecimal(6, offer.getPrice());
        pStmt.executeUpdate();
    }

    private boolean productExists() throws SQLException {
        PreparedStatement pStmt = conn.prepareStatement("SELECT * FROM product WHERE prod_number = ?");
        pStmt.setString(1, product.getProdNumber());
        ResultSet resultSet = pStmt.executeQuery();
        Product other = getProductFromResultSet(resultSet);
        return product.equals(other);
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
            if (resultSet.getArray("titles") != null) {
                String[] titles = (String[]) resultSet.getArray("titles").getArray();
                musicCd.setTitles(Arrays.asList(titles));
            } else {
                musicCd.setTitles(null);
            }
            obtainPersonRelations(musicCd);
            return musicCd;
        }
        return null;
    }

    private void persistBook() throws SQLException {
        PreparedStatement pStmt = conn.prepareStatement("INSERT INTO book (prod_number, page_number, " +
                "publication_date, isbn, publishers) VALUES (?, ?, ?, ?, ?)");
        pStmt.setString(1, product.getProdNumber());
        pStmt.setObject(2, ((Book) product).getPageNumber(), Types.INTEGER);
        pStmt.setObject(3, ((Book) product).getPublicationDate(), Types.DATE);
        pStmt.setString(4, ((Book) product).getIsbn());
        pStmt.setArray(5, conn.createArrayOf("VARCHAR", ((Book) product).getPublishers().toArray()));
        pStmt.executeUpdate();
    }

    private void persistMusicCd() throws SQLException {
        PreparedStatement pStmt = conn.prepareStatement("INSERT INTO music_cd (prod_number, labels, " +
                "publication_date, titles) VALUES (?, ?, ?, ?)");
        pStmt.setString(1, product.getProdNumber());
        pStmt.setArray(2, conn.createArrayOf("VARCHAR", ((MusicCd) product).getLabels().toArray()));
        if (((MusicCd) product).getPublicationDate() != null) {
            pStmt.setDate(3, Date.valueOf(((MusicCd) product).getPublicationDate()));
        } else {
            pStmt.setNull(3, Types.DATE);
        }
        if (!((MusicCd) product).getTitles().isEmpty()) {
            pStmt.setArray(4, conn.createArrayOf("VARCHAR", ((MusicCd) product).getTitles().toArray()));
        } else {
            pStmt.setNull(4, Types.ARRAY);
        }
        pStmt.executeUpdate();
    }

    private void persistDvd() throws SQLException {
        PreparedStatement pStmt = conn.prepareStatement("INSERT INTO dvd (prod_number, format, " +
                "duration_minutes, region_code) VALUES (?, ?, ?, ?)");
        pStmt.setString(1, product.getProdNumber());
        pStmt.setString(2, ((Dvd) product).getFormat());
        pStmt.setObject(3, ((Dvd) product).getDurationMinutes(), Types.INTEGER);
        pStmt.setShort(4, ((Dvd) product).getRegionCode());
        pStmt.executeUpdate();
    }

    private void persistPersonAndRelations() throws SQLException {
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
        PreparedStatement pStmtRelation = prepareRelationStatement(role);
        if (pStmtRelation == null) return;
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
            pStmtRelation.setInt(4, personId);
            pStmtRelation.executeUpdate();
        }
    }

    private PreparedStatement prepareRelationStatement(String role) throws SQLException {
        PreparedStatement pStmtRelation;
        switch(role) {
            case "artist":
                pStmtRelation = conn.prepareStatement("INSERT INTO cd_artist (cd, artist) " +
                        "VALUES (?, ?) ON CONFLICT (cd, artist) DO UPDATE SET cd = ?, artist = ?");
                break;
            case "author":
                pStmtRelation = conn.prepareStatement("INSERT INTO book_author (book, author) " +
                        "VALUES (?, ?) ON CONFLICT (book, author) DO UPDATE SET book = ?, author = ?");
                break;
            case "actor":
                pStmtRelation = conn.prepareStatement("INSERT INTO dvd_actor (dvd, actor) " +
                        "VALUES (?, ?) ON CONFLICT (dvd, actor) DO UPDATE SET dvd = ?, actor = ?");
                break;
            case "creator":
                pStmtRelation = conn.prepareStatement("INSERT INTO dvd_creator (dvd, creator) " +
                        "VALUES (?, ?) ON CONFLICT (dvd, creator) DO UPDATE SET dvd = ?, creator = ?");
                break;
            case "director":
                pStmtRelation = conn.prepareStatement("INSERT INTO dvd_director (dvd, director) " +
                        "VALUES (?, ?) ON CONFLICT (dvd, director) DO UPDATE SET dvd = ?, director = ?");
                break;
            default:
                conn.rollback();
                return null;
        }
        pStmtRelation.setString(1, product.getProdNumber());
        pStmtRelation.setString(3, product.getProdNumber());
        return pStmtRelation;
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
                pStmt = conn.prepareStatement("SELECT person FROM dvd_actor WHERE dvd = ?");
                col = "actor";
                break;
            case "creator":
                pStmt = conn.prepareStatement("SELECT person FROM dvd_creator WHERE dvd = ?");
                col = "creator";
                break;
            case "director":
                pStmt = conn.prepareStatement("SELECT person FROM dvd_director WHERE dvd = ?");
                col = "director";
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

    /**
     * tests whether an attribute really contains a relevant value (e.g. maybe value is empty string)
     * @param attributeValue
     * @return true if attributeValue is relevant
     */
    protected boolean attributeValueIsSpecified(String attributeValue) {
        return !(attributeValue.isBlank() || attributeValue.equalsIgnoreCase("not specified"));
    }

    /**
     * tests whether a text element really contains a relevant value (e.g. maybe value is empty string)
     * @param textElement
     * @return true if attributeValue is relevant
     */
    protected boolean textElementIsSpecified(String textElement) {
        return !(textElement.isBlank() || textElement.equalsIgnoreCase("not specified"));
    }
}

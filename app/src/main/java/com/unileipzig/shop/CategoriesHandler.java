package com.unileipzig.shop;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.*;
import java.util.ArrayList;
import java.util.Collection;

/**
 * Helper class for parsing the categories.xml file and writing the values to the database.
 */
public class CategoriesHandler extends DefaultHandler {

    protected static final String CATEGORIES = "categories";
    protected static final String CATEGORY = "category";
    protected static final String ITEM = "item";
    protected StringBuilder elementValue;
    protected ArrayList<Category> categories;
    protected Category current = null;
    protected Connection conn;
    protected PrintWriter printWriter;

    /**
     * Constructs an Instance of a CategoriesHandler, must be passed an established SQL connection and a file path
     * for writing error messages to.
     * @param conn an established SQL connection.
     * @param errorPath file path for writing error messages to.
     */
    public CategoriesHandler(Connection conn, String errorPath) throws IOException {
        this.printWriter = new PrintWriter(new FileWriter(errorPath));
        this.conn = conn;
    }

    /**
     * @inheritDoc
     */
    @Override
    public void characters(char[] ch, int start, int length) throws SAXException {
        if (elementValue == null) {
            elementValue = new StringBuilder();
        } else {
            elementValue.append(ch, start, length);
        }
    }

    /**
     * @inheritDoc
     */
    @Override
    public void startDocument() {
        elementValue = new StringBuilder();
        categories = new ArrayList<>();
    }

    /**
     * @inheritDoc
     */
    @Override
    public void startElement(String uri, String lName, String qName, Attributes attr) throws SAXException {
        switch (qName) {
            case CATEGORY:
                addCategoryNameIfNotSet();
                Category c = new Category();
                if (current == null) {
                    categories.add(c);
                } else {
                    current.addChild(c);
                }
                current = c;
                break;
            case ITEM:
                addCategoryNameIfNotSet();
                break;
        }
        elementValue.setLength(0);
    }

    /**
     * @inheritDoc
     */
    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
        switch (qName) {
            case CATEGORY:
                addCategoryNameIfNotSet();
                current = current.getParent();
                break;
            case ITEM:
                current.addItem(elementValue.toString());
                elementValue = new StringBuilder();
                break;
        }
    }

    /**
     * @inheritDoc
     */
    @Override
    public void endDocument() throws SAXException {
        super.endDocument();
        writeToDataBase();
        printWriter.close();
    }

    private void addCategoryNameIfNotSet() {
        if (!elementValue.toString().isBlank()) {
            current.setName(elementValue.toString());
            elementValue = new StringBuilder();
        }
    }

    private void writeToDataBase() {
        addCategoriesRecursively(categories, conn, null);
    }

    private void addCategoriesRecursively(Collection<Category> categories, Connection conn, Category parent) {
        for (Category category : categories) {
            addCategoryAndSetID(category, conn);
            setSubCategory(parent, category, conn);
            for (String item : category.getItems()) {
                addItemToCategory(item, category, conn);
            }
            addCategoriesRecursively(category.getChildren(), conn, category);
        }
    }

    private void addCategoryAndSetID(Category category, Connection conn) {
        String insertQuery = "INSERT INTO category (name) VALUES (?)";
        try (PreparedStatement insertStmt = conn.prepareStatement(insertQuery, Statement.RETURN_GENERATED_KEYS)) {
            insertStmt.setString(1, category.getName());
            insertStmt.executeUpdate();
            ResultSet generatedKeys = insertStmt.getGeneratedKeys();
            generatedKeys.next();
            category.setID(generatedKeys.getInt(1));
        } catch (SQLException e) {
            e.printStackTrace();
            printWriter.println(e.getMessage());
        }

    }

    private boolean addItemToCategory(String product_number, Category category, Connection conn) {
        String insertQuery = "INSERT INTO product_category (product, category) VALUES (?, ?)";
        try (PreparedStatement insertStmt = conn.prepareStatement(insertQuery)) {
            insertStmt.setString(1, product_number);
            insertStmt.setInt(2, category.getID());
            insertStmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            printWriter.println(e.getMessage());
            return false;
        }
        return true;
    }

    private boolean setSubCategory(Category parent, Category child, Connection conn) {
        if (parent == null) {
            return true;
        }
        String insertQuery = "INSERT INTO category_hierarchy (super_category, sub_category) VALUES (?, ?)";
        try (PreparedStatement insertStmt = conn.prepareStatement(insertQuery)) {
            insertStmt.setInt(1, parent.getID());
            insertStmt.setInt(2, child.getID());
            insertStmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            printWriter.println(e.getMessage());
            return false;
        }
        return true;
    }
}

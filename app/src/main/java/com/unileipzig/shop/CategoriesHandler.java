package com.unileipzig.shop;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;

public class CategoriesHandler extends DefaultHandler {
    private static final String CATEGORIES = "categories";
    private static final String CATEGORY = "category";
    private static final String ITEM = "item";

    private StringBuilder elementValue;
    private ArrayList<Category> categories;
    private Category current = null;
    private Connection conn;

    public CategoriesHandler(Connection conn) {
        this.conn = conn;
    }

    @Override
    public void characters(char[] ch, int start, int length) throws SAXException {
        if (elementValue == null) {
            elementValue = new StringBuilder();
        } else {
            elementValue.append(ch, start, length);
        }
    }

    @Override
    public void startDocument() throws SAXException {
        elementValue = new StringBuilder();
        categories = new ArrayList<>();
    }

    @Override
    public void startElement(String uri, String lName, String qName, Attributes attr) throws SAXException {
        switch (qName) {
            case CATEGORIES:

                break;
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
    }

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

    @Override
    public void endDocument() throws SAXException {
        super.endDocument();
        writeToDataBase();
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
            addCategoryIfNotExists(category, conn);
            setSubCategory(parent, category, conn);
            for (String item : category.getItems()) {
                addItemToCategory(item, category, conn);
            }
            addCategoriesRecursively(category.getChildren(), conn, category);
        }
    }

    private void addCategoryIfNotExists(Category category, Connection conn) {
        String insertQuery = "INSERT INTO category VALUES (?) ON CONFLICT DO NOTHING";
        try (PreparedStatement insertStmt = conn.prepareStatement(insertQuery)) {
            insertStmt.setString(1, category.getName());
            insertStmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            //TODO handle SQL Exception
        }

    }

    private boolean addItemToCategory(String prod_num_str, Category category, Connection conn) {
        String response = checkProductNumber(prod_num_str);
        if (!response.equals("OK")) {
            // TODO handle bad response
            return false;
        }
        int product_number = Integer.parseInt(prod_num_str);
        String insertQuery = "INSERT INTO product_category (product, category) VALUES (?, ?)";
        try (PreparedStatement insertStmt = conn.prepareStatement(insertQuery)) {
            insertStmt.setInt(1, product_number);
            insertStmt.setString(2, category.getName());
            insertStmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            //TODO handle SQL Exception
            return false;
        }
        return true;
    }

    private boolean setSubCategory(Category parent, Category child, Connection conn) {
        if (parent == null) {
            return true;
        }
        String insertQuery = "INSERT INTO category_hierarchy (super_category, sub_category) VALUES (?, ?) ON CONFLICT (super_category, sub_category) DO NOTHING";
        try (PreparedStatement insertStmt = conn.prepareStatement(insertQuery)) {
            insertStmt.setString(1, parent.getName());
            insertStmt.setString(2, child.getName());
            insertStmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            //TODO handle SQL Exception
            return false;
        }
        return true;
    }

    private String checkProductNumber(String prod_num_str) {
        try {
            Integer.parseInt(prod_num_str);
        } catch (NumberFormatException e) {
            return e.getMessage();
        }
        return "OK";
    }
}

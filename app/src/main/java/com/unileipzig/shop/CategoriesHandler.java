package com.unileipzig.shop;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Stack;

public class CategoriesHandler extends DefaultHandler {
    private static final String CATEGORIES = "categories";
    private static final String CATEGORY = "category";
    private static final String ITEM = "item";

    private StringBuilder elementValue;
    private Stack<Category> categories;
    private int categoryID;
    private Connection conn;

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
        // TODO establish connection
        elementValue = new StringBuilder();
    }

    @Override
    public void startElement(String uri, String lName, String qName, Attributes attr) throws SAXException {
        switch (qName) {
            case CATEGORIES:
                categories = new Stack<>();
                break;
            case CATEGORY:
                if (!categories.empty()) {
                    Category parent = categories.peek();
                    parent.name = elementValue.toString();
                    elementValue = new StringBuilder();
                }
                categories.push(new Category(null, categories.empty()));
                break;
            case ITEM:
                if (!elementValue.toString().equals("")) {
                    categories.peek().name = elementValue.toString();
                    elementValue = new StringBuilder();
                }
                break;
        }
    }

    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
        switch (qName) {
            case CATEGORY:
                if (!elementValue.toString().equals("")) {
                    categories.peek().name = elementValue.toString();
                }
                Category current = categories.pop();
                addCategory(current);
                if (!categories.empty()) {
                    Category parent = categories.peek();
                    setSubCategory(parent, current);
                }
                break;
            case ITEM:
                addItemToCategory(elementValue.toString(), categories.peek());
                elementValue = new StringBuilder();
                break;
        }
    }

    private void addCategory(Category category) {
        String insertQuery = "INSERT INTO category (name, is_main) VALUES (?, ?)";
        try (PreparedStatement insertStmt = conn.prepareStatement(insertQuery)) {
            insertStmt.setString(1, category.name);
            insertStmt.setBoolean(2, category.isMain); //TODO remove
            insertStmt.executeUpdate();
        } catch (SQLException e) {
            //TODO handle SQL Exception
        }
    }

    private boolean addItemToCategory(String prod_num_str, Category category) {
        String response = checkProductNumber(prod_num_str);
        if (!response.equals("OK")) {
            // TODO handle bad response
            return false;
        }
        int product_number = Integer.parseInt(prod_num_str);
        String insertQuery = "INSERT INTO product_category (product, category) VALUES (?, ?)";
        try (PreparedStatement insertStmt = conn.prepareStatement(insertQuery)) {
            insertStmt.setInt(1, product_number);
            insertStmt.setString(2, category.name);
            insertStmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            //TODO handle SQL Exception
            return false;
        }
    }

    private String checkProductNumber(String prod_num_str) {
        try {
            Integer.parseInt(prod_num_str);
        } catch (NumberFormatException e) {
            return e.getMessage();
        }
        return "OK";
    }

    private void setSubCategory(Category parent, Category child) {
        //TODO SQL stuff
    }
}

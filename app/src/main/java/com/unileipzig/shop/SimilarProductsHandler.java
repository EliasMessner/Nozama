package com.unileipzig.shop;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class SimilarProductsHandler extends DefaultHandler {

    protected StringBuilder currentValue;
    protected String product_number;
    protected Connection conn;
    protected Map<String, List<String>> similarProducts;
    protected PrintWriter printWriter;
    protected boolean isInSimilars;

    public SimilarProductsHandler(Connection conn, String errorFilePath) throws IOException {
        this.similarProducts = new HashMap<>();
        this.conn = conn;
        this.currentValue = new StringBuilder();
        this.printWriter = new PrintWriter(new FileWriter(errorFilePath));
        this.isInSimilars = false;
    }

    @Override
    public void endDocument() {
        writeToDatabase();
        printWriter.close();
    }

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) {
        currentValue.setLength(0);
        switch (qName) {
            case "item":
                handleItemStartTag(attributes);
                break;
            case "similars":
                isInSimilars = true;
                break;
        }
    }

    abstract protected void handleItemStartTag(Attributes attributes);

    @Override
    public void endElement(String uri, String localName, String qName) {
        if (qName.equals("similars")) {
            isInSimilars = false;
        }
    }

    @Override
    public void characters(char[] ch, int start, int length) throws SAXException {
        if (currentValue == null) {
            currentValue = new StringBuilder();
        } else {
            currentValue.append(ch, start, length);
        }
    }

    protected void addSimilarRelation(String prod1, String prod2) {
        if (!this.similarProducts.containsKey(prod1)) {
            this.similarProducts.put(prod1, new ArrayList<String>());
        }
        this.similarProducts.get(prod1).add(prod2);
    }

    protected void writeToDatabase() {
        try {
            PreparedStatement pStmt = conn.prepareStatement("INSERT INTO similar_products (product1, product2) VALUES (?, ?)");
            for (String prod_number1 : similarProducts.keySet()) {
                pStmt.setString(1, prod_number1);
                for (String prod_number2 : similarProducts.get(prod_number1)) {
                    pStmt.setString(2, prod_number2);
                    pStmt.executeUpdate();
                }
            }
        } catch (SQLException throwables) {
            printWriter.println(throwables.getMessage());
            throwables.printStackTrace();
        }
    }
}

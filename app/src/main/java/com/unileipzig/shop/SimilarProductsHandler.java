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

/**
 * Abstract helper class for parsing the xml files containing the leipzig and dresden products and writing the values to
 * the database.
 * This class is a generalization for everything the leipzig and dresden files have in common, and will be overridden
 * by concrete implementations for each respective store.
 */
public abstract class SimilarProductsHandler extends DefaultHandler {

    protected StringBuilder currentValue;
    protected String product_number;
    protected Connection conn;
    protected Map<String, List<String>> similarProducts;
    protected PrintWriter printWriter;
    protected boolean isInSimilars;

    /**
     * Constructs an Instance of the helper class with an established database connection and an error-file path.
     * @param conn an established database connection
     * @param errorFilePath error-file path to write error messages to. Will be re-created each time.
     * @throws IOException Any IOException thrown by the printWriter.
     */
    public SimilarProductsHandler(Connection conn, String errorFilePath) throws IOException {
        this.similarProducts = new HashMap<>();
        this.conn = conn;
        this.currentValue = new StringBuilder();
        this.printWriter = new PrintWriter(new FileWriter(errorFilePath));
        this.isInSimilars = false;
    }

    /**
     * {@inheritDoc}
     * Dumps the parsed similar-products structure to the database and closes the printWriter.
     */
    @Override
    public void endDocument() {
        writeToDatabase();
        printWriter.close();
    }

    /**
     * {@inheritDoc}
     * Will behave according to the name of the start tag.
     */
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

    /**
     * Must be overridden by the concrete subclasses. Behavior will then depend on the xml structure.
     * @param attributes the attributes of the current start tag.
     */
    abstract protected void handleItemStartTag(Attributes attributes);

    /**
     * {@inheritDoc}
     * Will behave according to the name of the start tag.
     */
    @Override
    public void endElement(String uri, String localName, String qName) {
        if (qName.equals("similars")) {
            isInSimilars = false;
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void characters(char[] ch, int start, int length) throws SAXException {
        if (currentValue == null) {
            currentValue = new StringBuilder();
        } else {
            currentValue.append(ch, start, length);
        }
    }

    /**
     * Puts a relation of two similar products into this model.
     * @param prod1 the first product
     * @param prod2 the second product
     */
    protected void addSimilarRelation(String prod1, String prod2) {
        if (!this.similarProducts.containsKey(prod1)) {
            this.similarProducts.put(prod1, new ArrayList<String>());
        }
        this.similarProducts.get(prod1).add(prod2);
    }

    private void writeToDatabase() {
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

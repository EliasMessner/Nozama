package com.unileipzig.shop;

import org.xml.sax.SAXException;

import javax.xml.crypto.Data;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Hello world!
 *
 */
public class App
{

    private static final String FILENAME_LEIPZIG = "/data/leipzig_transformed.xml";
    private static final String FILENAME_DRESDEN = "/data/dresden.xml";
    private static final String FILENAME_CATEGORIES = "/data/categories.xml";

    public static void main( String[] args )
    {
        SAXParserFactory factory = SAXParserFactory.newInstance();
        Connection conn = DatabaseConnector.getConnection("test_elias");
        try {
            SAXParser saxParser = factory.newSAXParser();
            ProductHandler productHandler = new ProductHandler(conn);
            ProductHandlerDresden productHandlerDresden = new ProductHandlerDresden(conn);
            //CategoriesHandler categoriesHandler = new CategoriesHandler(conn);
            saxParser.parse(FILENAME_LEIPZIG, productHandler);
            saxParser.parse(FILENAME_DRESDEN, productHandlerDresden);
            //saxParser.parse(FILENAME_CATEGORIES, categoriesHandler);
        } catch (ParserConfigurationException | SAXException | IOException e) {
            e.printStackTrace();
        }
    }
}

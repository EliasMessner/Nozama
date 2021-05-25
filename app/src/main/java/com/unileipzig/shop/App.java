package com.unileipzig.shop;

import org.xml.sax.SAXException;

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
    private static final String FILENAME_DRESDEN = "/data/leipzig.xml";
    private static final String FILENAME_CATEGORIES = "/data/categories.xml";

    public static void main( String[] args )
    {
        System.out.println( "Hello World!" );

        try {
            Class.forName("org.postgresql.Driver");
            Connection conn = DriverManager.getConnection("jdbc:postgresql://db:5432/postgres",
                    "postgres", "example") ;

            if (conn != null) {
                System.out.println("Connected to the database!");
            } else {
                System.out.println("Failed to make connection!");
            }

            SAXParserFactory factory = SAXParserFactory.newInstance();
            SAXParser saxParser = factory.newSAXParser();

            CategoriesHandler categoriesHandler = new CategoriesHandler();
            saxParser.parse(FILENAME_CATEGORIES, categoriesHandler);
            // categoriesHandler.writeToDataBase(conn);

            /*
            ProductHandler handler = new ProductHandler();
            saxParser.parse(FILENAME_LEIPZIG, handler);
             */

        } catch (SQLException e) {
            System.err.format("SQL State: %s\n%s", e.getSQLState(), e.getMessage());
            e.printStackTrace();
        } catch (ParserConfigurationException | SAXException | IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

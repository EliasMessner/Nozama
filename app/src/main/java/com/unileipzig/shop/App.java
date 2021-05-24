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

        SAXParserFactory factory = SAXParserFactory.newInstance();

        try {

            SAXParser saxParser = factory.newSAXParser();

            ProductHandler handler = new ProductHandler();
            saxParser.parse(FILENAME_LEIPZIG, handler);

        } catch (ParserConfigurationException | SAXException | IOException e) {
            e.printStackTrace();
        }
    }
}

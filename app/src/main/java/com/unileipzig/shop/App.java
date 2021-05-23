package com.unileipzig.shop;

import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.IOException;

/**
 * Hello world!
 *
 */
public class App 
{

    private static final String FILENAME = "/data/leipzig_transformed.xml";

    public static void main( String[] args )
    {
        System.out.println( "Hello World!" );

        SAXParserFactory factory = SAXParserFactory.newInstance();

        try {

            SAXParser saxParser = factory.newSAXParser();

            ProductHandler handler = new ProductHandler();
            saxParser.parse(FILENAME, handler);

        } catch (ParserConfigurationException | SAXException | IOException e) {
            e.printStackTrace();
        }
    }
}

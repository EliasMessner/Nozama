package com.unileipzig.shop;

import org.xml.sax.SAXException;

import javax.xml.crypto.Data;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
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
        Connection conn = DatabaseConnector.getConnection();
        try {
            PrintWriter printWriter = new PrintWriter(new FileWriter("/data/errors.txt"));
            SAXParser saxParser = factory.newSAXParser();
            ProductHandler productHandler = new ProductHandler(conn);
            ProductHandlerDresden productHandlerDresden = new ProductHandlerDresden(conn);
            SimilarProductsHandlerLeipzig similarProductsHandlerLeipzig = new SimilarProductsHandlerLeipzig(conn);
            SimilarProductsHandlerDresden similarProductsHandlerDresden = new SimilarProductsHandlerDresden(conn);
            CategoriesHandler categoriesHandler = new CategoriesHandler(conn);

            printWriter.println("\n################## LEIPZIG PRODUCTS ####################\n");
            saxParser.parse(FILENAME_LEIPZIG, productHandler);
            printWriter.println("\n################## DRESDEN PRODUCTS ####################\n");
            saxParser.parse(FILENAME_DRESDEN, productHandlerDresden);

            printWriter.println("\n################## LEIPZIG SIMILARS ####################\n");
            saxParser.parse(FILENAME_LEIPZIG, similarProductsHandlerLeipzig);
            printWriter.println("\n################## DRESDEN SIMILARS ####################\n");
            saxParser.parse(FILENAME_DRESDEN, similarProductsHandlerDresden);
            printWriter.println("\n################## CATEGORIES ####################\n");
            saxParser.parse(FILENAME_CATEGORIES, categoriesHandler);


            printWriter.close();
        } catch (ParserConfigurationException | SAXException | IOException e) {
            e.printStackTrace();
        }
    }
}

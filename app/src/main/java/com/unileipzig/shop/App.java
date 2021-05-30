package com.unileipzig.shop;

import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.IOException;
import java.sql.Connection;

/**
 * Hello world!
 *
 */
public class App
{

    private static final String FILENAME_LEIPZIG = "/data/leipzig_transformed.xml";
    private static final String FILENAME_DRESDEN = "/data/dresden.xml";
    private static final String FILENAME_CATEGORIES = "/data/categories.xml";
    private static final String FILENAME_REVIEWS = "/data/reviews.csv";

    public static void main( String[] args )
    {
        SAXParserFactory factory = SAXParserFactory.newInstance();
        Connection conn = DatabaseConnector.getConnection("test_1");
        try {
            SAXParser saxParser = factory.newSAXParser();
            ProductHandler productHandler = new ProductHandler(conn, "/data/errors.txt");
            ProductHandlerDresden productHandlerDresden = new ProductHandlerDresden(conn, "/data/errorsDresden.txt");
            CategoriesHandler categoriesHandler = new CategoriesHandler(conn, "/data/errorsCategories.txt");
            ReviewHandler reviewHandler = new ReviewHandler(FILENAME_REVIEWS, conn, "/data/errorsReviews.txt");
            SimilarProductsHandlerLeipzig similarProductsHandlerLeipzig = new SimilarProductsHandlerLeipzig(conn, "/data/errorsSimilarsLeipzig.txt");
            SimilarProductsHandlerDresden similarProductsHandlerDresden = new SimilarProductsHandlerDresden(conn, "/data/errorsSimilasDresden.txt");
            saxParser.parse(FILENAME_LEIPZIG, productHandler);
            saxParser.parse(FILENAME_DRESDEN, productHandlerDresden);
            saxParser.parse(FILENAME_CATEGORIES, categoriesHandler);
            saxParser.parse(FILENAME_LEIPZIG, similarProductsHandlerLeipzig);
            saxParser.parse(FILENAME_DRESDEN, similarProductsHandlerDresden);
            reviewHandler.handle();
        } catch (ParserConfigurationException | SAXException | IOException e) {
            e.printStackTrace();
        }
    }
}

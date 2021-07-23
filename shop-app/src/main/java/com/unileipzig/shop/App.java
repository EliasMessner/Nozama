package com.unileipzig.shop;

import com.unileipzig.shop.controller.MainController;
import com.unileipzig.shop.exceptions.AmbiguousCategoryNameException;
import com.unileipzig.shop.exceptions.CategoryNotFoundException;
import com.unileipzig.shop.model.*;
import org.hibernate.Session;

import java.math.BigDecimal;
import java.util.List;
import java.util.Scanner;

public class App {

    public static void main( String[] args ) throws ClassNotFoundException, InputException {
        MainController mainController = new MainController();

        mainController.init();

//        Session session = HibernateConnector.getSession();
//        session.beginTransaction();
//
//        Store store = new Store("Cottbus", "Friedrich-Ebert-Str. 18", "03042");
//        session.save(store);
//
//        Book book = new Book( "123", "product1" );
//        book.addPublisher("ps1");
//        book.addPublisher("ps2");
//        book.addAuthor(new Person("Tim Sternley"));
//        session.save(book);
//
//        Dvd dvd = new Dvd("456", "dvd1");
//        dvd.setRating(4.3);
//        dvd.addActor(new Person("Crazy Fun"));
//        dvd.setDurationMinutes(23);
//        dvd.addSimilarProduct(book);
//        session.save(dvd);
//
//        Offer offer = new Offer(dvd, store);
//        offer.setArticleCondition("new");
//        offer.setPrice(new BigDecimal(20));
//        session.save(offer);
//
//        Offer offer2 = new Offer(book, store);
//        offer2.setArticleCondition("new");
//        offer2.setPrice(new BigDecimal(15));
//        session.save(offer2);
//
//        Offer offer3 = new Offer(book, store);
//        offer3.setArticleCondition("second-hand");
//        offer3.setPrice(new BigDecimal("12.5"));
//        session.save(offer3);
//
//        session.getTransaction().commit();
//        session.close();
//
//        Product product = mainController.getProduct("456");
//        System.out.println(product.getTitle());
//
//        List<Product> productsFound = mainController.getProducts("%1");
//        System.out.println(productsFound.size());
//
//        List<Product> topProducts = mainController.getTopProducts(1);
//        System.out.println(topProducts.get(0).getTitle());
//
//        List<Product> similarCheaperProducts = mainController.getSimilarCheaperProducts("456");
//        System.out.println(similarCheaperProducts.get(0).getTitle());
//
//        List<Offer> offerList = mainController.getOffers("123");
//        System.out.println(offerList.size());
//
//        System.out.println("##################################");

//        Category root = mainController.getCategoryTree();

//        try {
//            List<Product> products = mainController.getProductsByCategoryPath("/Formate/Box-Sets/Alternative");
//            products.forEach(p -> System.out.println(p.getTitle()));
//        } catch (CategoryNotFoundException | AmbiguousCategoryNameException e) {
//            e.printStackTrace();
//        }

//        mainController.addNewReview("baerchen76", "B0000668PG", 3, "ganz ok", "ist echt kanz ok.");

//        List<Customer> trolls = mainController.getTrolls(4);
//        System.out.printf("Trolls: (found %d)\n", trolls.size());
//        trolls.forEach(troll -> System.out.println(troll.getUsername()));

        Scanner scanner = new Scanner(System.in);
        System.out.println("Database ready. Enter command or type 'help'.");

        boolean repeat = true;
        while (repeat) {
            if (!scanner.hasNextLine()) continue;
            String input = scanner.nextLine().toLowerCase();
            String[] inputTokens = input.split("[() ,]+");
            switch (inputTokens[0]) {
                case "help":
                    printHelp();
                    break;
                case "finish":
                    mainController.finish();
                    System.out.println("Goodbye.");
                    repeat = false;
                    break;
                case "getcategorytree":
                    printCategoryTree(mainController);
                    break;
                case "gettrolls":
                    tryPrintTrolls(mainController, inputTokens);
                    break;
                case "addnewreview":
                    tryAddNewReview(mainController, inputTokens);
                    break;
                case "getproductsbycategorypath":
                    tryGetProductsByCatPath(mainController, inputTokens);
                    break;
                case "getproduct":
                    tryPrintProduct(mainController, inputTokens);
                    break;
                case "gettopproducts":
                    tryGetTopProducts(mainController, inputTokens);
                    break;
                case "getsimilarcheaperproducts":
                    tryGetSimilarCheaperProducts(mainController, inputTokens);
                    break;
                case "getoffers":
                    tryGetOffers(mainController, inputTokens);
                    break;
                default:
                    System.out.printf("Unrecognized command '%s'", inputTokens[0]);
            }
        }
    }

    private static void tryGetOffers(MainController mainController, String[] inputTokens) {
        // TODO
    }

    private static void tryGetSimilarCheaperProducts(MainController mainController, String[] inputTokens) {
        // TODO
    }

    private static void tryGetTopProducts(MainController mainController, String[] inputTokens) {
        // TODO
    }

    private static void tryPrintProduct(MainController mainController, String[] inputTokens) {
        // TODO
    }

    private static void tryGetProductsByCatPath(MainController mainController, String[] inputTokens) {
        // TODO
    }

    private static void tryAddNewReview(MainController mainController, String[] inputTokens) {
        // TODO
    }

    private static void printCategoryTree(MainController mainController) {
        Category root = mainController.getCategoryTree();
        System.out.println(root.toString());
    }

    private static void tryPrintTrolls(MainController mainController, String[] inputTokens) {
        if (inputTokens.length > 2) {
            System.out.printf("Too many arguments. Expected 1, got %d.%n", inputTokens.length - 1);
        }
        try {
            int ratingLimit = Integer.parseInt(inputTokens[1]);
            List<Customer> trolls = mainController.getTrolls(ratingLimit);
            System.out.printf("Trolls (found %d):%n", trolls.size());
            trolls.forEach(t -> System.out.println(t.getUsername()));
        } catch (NumberFormatException e) {
            System.out.printf("Invalid rating-limit: '%s'%n", inputTokens[1]);
        }
    }

    private static void printHelp() {
        // TODO
    }
}

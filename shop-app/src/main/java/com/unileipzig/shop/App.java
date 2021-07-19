package com.unileipzig.shop;

import com.unileipzig.shop.controller.MainController;
import com.unileipzig.shop.model.*;
import org.hibernate.Session;

import java.math.BigDecimal;
import java.util.List;

public class App {

    public static void main( String[] args ) throws ClassNotFoundException, InputException {
        MainController mainController = new MainController();

        mainController.init();

        Session session = HibernateConnector.getSession();
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

        System.out.println("##################################");

        mainController.addNewReview("baerchen76", "B0000668PG", 3, "ganz ok", "ist echt kanz ok.");

//        List<Customer> trolls = mainController.getTrolls(4);
//        System.out.printf("Trolls: (found %d)", trolls.size());
//        trolls.forEach(troll -> System.out.println(troll.getUsername()));

        mainController.finish();
    }
}

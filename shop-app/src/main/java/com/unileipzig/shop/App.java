package com.unileipzig.shop;

import com.unileipzig.shop.controller.MainController;
import com.unileipzig.shop.model.Book;
import com.unileipzig.shop.model.Dvd;
import com.unileipzig.shop.model.Person;
import com.unileipzig.shop.model.Product;
import org.hibernate.Session;

public class App {

    public static void main( String[] args ) throws ClassNotFoundException {
        MainController mainController = new MainController();

        mainController.init();

        Session session = HibernateConnector.getSession();
        session.beginTransaction();
        Book book = new Book( "123", "product1" );
        book.addPublisher("ps1");
        book.addPublisher("ps2");
        book.addAuthor(new Person("Tim Sternley"));
        session.save(book);
        session.getTransaction().commit();
        session.beginTransaction();
        Dvd dvd = new Dvd("456", "dvd1");
        dvd.addActor(new Person("Crazy Fun"));
        dvd.setDurationMinutes(23);
        session.save(dvd);
        session.getTransaction().commit();
        session.close();

        mainController.finish();
    }
}

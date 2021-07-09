package com.unileipzig.shop;

import com.unileipzig.shop.controller.MainController;
import com.unileipzig.shop.model.Product;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;

public class App {

    public static void main( String[] args ) throws ClassNotFoundException {
        MainController mainController = new MainController();

        mainController.init();

        Session session = HibernateConnector.getSession();
        session.beginTransaction();
        session.save( new Product( "123", "product1" ) );
        session.save( new Product( "555", "product2" ) );
        session.getTransaction().commit();
        session.close();

        mainController.finish();
    }
}

package com.unileipzig.shop;

import com.unileipzig.shop.model.Product;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;

public class App {

    public static void main( String[] args ) throws ClassNotFoundException {
        // A SessionFactory is set up once for an application!
        final StandardServiceRegistry registry = new StandardServiceRegistryBuilder()
                .configure() // configures settings from hibernate.cfg.xml
                .build();
        SessionFactory sessionFactory = null;
        try {
            sessionFactory = new MetadataSources( registry ).buildMetadata().buildSessionFactory();
        }
        catch (Exception e) {
            // The registry would be destroyed by the SessionFactory, but we had trouble building the SessionFactory
            // so destroy it manually.
            System.out.println(e.getMessage());
            StandardServiceRegistryBuilder.destroy( registry );
        }

        Session session = sessionFactory.openSession();
        session.beginTransaction();
        session.save( new Product( "123", "product1" ) );
        session.save( new Product( "555", "product2" ) );
        session.getTransaction().commit();
        session.close();

        sessionFactory.close();
    }
}

package com.unileipzig.shop;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;

public class HibernateConnector {

    private static SessionFactory sessionFactory;
    private static final Session[] sessions = new Session[1];

    public static void initSessionFactory() {
        // A SessionFactory is set up once for an application!
        final StandardServiceRegistry registry = new StandardServiceRegistryBuilder()
                .configure() // configures settings from hibernate.cfg.xml
                .build();
        try {
            sessionFactory = new MetadataSources( registry ).buildMetadata().buildSessionFactory();
        }
        catch (Exception e) {
            // The registry would be destroyed by the SessionFactory, but we had trouble building the SessionFactory
            // so destroy it manually.
            System.out.println(e.getMessage());
            StandardServiceRegistryBuilder.destroy( registry );
        }

    }

    public static void initSession() {
        sessions[0] = sessionFactory.openSession();
    }

    public static void finishSessionFactory() {
        sessionFactory.close();
    }

    public static void finishSessions() {
        for (Session session: sessions) {
            if (session != null) {
                session.close();
            }
        }
    }

    public static Session getSession() {
        return sessions[0];
    }
}

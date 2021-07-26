package com.unileipzig.shop.repository;

import com.unileipzig.shop.HibernateConnector;
import com.unileipzig.shop.model.Customer;
import com.unileipzig.shop.model.Review;
import org.hibernate.Session;
import org.hibernate.query.Query;

import java.util.List;

public class CustomerRepository {

    public Customer getCustomer(String username) {
        Session session = HibernateConnector.getSession();
        return session.get(Customer.class, username);
    }

    public List<Customer> getCustomers() {
        Session session = HibernateConnector.getSession();
        Query<Customer> query = session.createQuery("FROM Customer", Customer.class);
        return query.list();
    }
}

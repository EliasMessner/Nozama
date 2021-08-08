package com.unileipzig.shop.repository;

import com.unileipzig.shop.HibernateConnector;
import com.unileipzig.shop.model.Category;
import com.unileipzig.shop.model.Review;
import org.hibernate.Session;
import org.hibernate.query.Query;
import java.util.List;
import java.util.stream.Collectors;

public class CategoryRepository {

    public Category getCategory(int id) {
        Session session = HibernateConnector.getSession();
        return session.get(Category.class, id);
    }

    public List<Category> getCategories() {
        Session session = HibernateConnector.getSession();
        Query<Category> query = session.createQuery("FROM Category", Category.class);
        return query.list();
    }

    public List<Category> getChildren(int id) {
        Session session = HibernateConnector.getSession();
        Query<Category> query = session.createQuery("SELECT DISTINCT c.children FROM Category c INNER JOIN c.children WHERE " +
                "c.ID = :id", Category.class);
        query.setParameter("id", id);
        return query.list();
    }

    public List<Category> getParents(int id) {
        Session session = HibernateConnector.getSession();
        Query<Category> query = session.createQuery("SELECT DISTINCT c FROM Category c INNER JOIN c.children child WHERE " +
                "child.ID = :id", Category.class);
        query.setParameter("id", id);
        return query.list();
    }

    public List<Category> getMainCategories() {
        Session session = HibernateConnector.getSession();
        Query<Category> query = session.createQuery("SELECT DISTINCT c1 FROM Category c1 INNER JOIN c1.children " +
                "WHERE NOT EXISTS " +
                "(SELECT c2 FROM Category c2 INNER JOIN c2.children child WHERE child.ID = c1.ID)", Category.class);
        return query.list();

//        return getCategories().stream()
//                .filter(category -> getParents(category.getId()).isEmpty())
//                .collect(Collectors.toList());
    }
}

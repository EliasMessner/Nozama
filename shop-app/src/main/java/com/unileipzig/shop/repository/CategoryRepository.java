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
        Query<Category> query = session.createQuery("SELECT c.children FROM Category c INNER JOIN c.children WHERE " +
                "c.ID = :id", Category.class);
        return query.list();
    }

    public Category getParent(int id) {
        Category child = getCategory(id);
        List<Category> categories = getCategories();
        for (Category c : categories) {
            if (c.getChildren().contains(child)) {
                return c;
            }
        }
        return null;
    }

    public List<Category> getMainCategories() {
        return getCategories().stream()
                .filter(category -> getParent(category.getId()) == null)
                .collect(Collectors.toList());
    }
}

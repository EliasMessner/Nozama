package com.unileipzig.shop.model;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Entity
@DynamicInsert
@DynamicUpdate
public class Category {

    private String name;

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private long ID;

    @ManyToMany()
    @JoinTable(name = "category_hierarchy", joinColumns = @JoinColumn(name = "super_category"),
            inverseJoinColumns = @JoinColumn(name = "sub_category"))
    private List<Category> children;

    @ManyToMany()
    @JoinTable(name = "product_category", joinColumns = @JoinColumn(name = "category"),
            inverseJoinColumns = @JoinColumn(name = "product"))
    private List<Product> products;

    public Category() {}

    /**
     * Constructs a category with empty list for children, and specified name. Parent will be
     * initialized as null.
     * @param name the name of this category.
     */
    public Category(String name) {
        this.products = new ArrayList<>();
        this.children = new ArrayList<>();
        this.name = name;
    }

    /**
     * Adds a child category to the list of children.
     * @param child the child to be added.
     */
    public void addChild(Category child) {
        children.add(child);
    }

    public List<Category> getChildren() {
        return children;
    }

    public long getId() {
        return ID;
    }

    public String getName() {
        return name;
    }

    public List<Product> getProducts() {
        return products;
    }

    public String toString() {
        StringBuilder buffer = new StringBuilder(50);
        print(buffer, "", "");
        return buffer.toString();
    }

    private void print(StringBuilder buffer, String prefix, String childrenPrefix) {
        buffer.append(prefix).append(name).append("\n");
        for (Iterator<Category> it = children.iterator(); it.hasNext();) {
            Category next = it.next();
            if (it.hasNext()) {
                next.print(buffer, childrenPrefix + "├── ", childrenPrefix + "│   ");
            } else {
                next.print(buffer, childrenPrefix + "└── ", childrenPrefix + "    ");
            }
        }
    }
}

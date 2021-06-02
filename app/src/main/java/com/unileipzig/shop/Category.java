package com.unileipzig.shop;

import java.util.ArrayList;

/**
 * Model for table category. Will also store children and parents for table category_hierarchy.
 */
public class Category {

    private String name;
    private ArrayList<Category> children;
    private Category parent;
    private ArrayList<String> items;
    private int ID;

    /**
     * Constructs a category with empty lists for items and children, name and parent will be initialized as null.
     */
    public Category() {
        this.children = new ArrayList<>();
        this.items = new ArrayList<>();
        this.name = null;
        this.parent = null;
    }

    /**
     * Constructs a category with empty lists for items and children, and specified name. Parent will be
     * initialized as null.
     * @param name the name of this category.
     */
    public Category(String name) {
        this.children = new ArrayList<>();
        this.items = new ArrayList<>();
        this.name = name;
        this.parent = null;
    }

    /**
     * Constructs a category with empty lists for items and children, and specified name and parent category.
     * @param name the name of this category.
     * @param parent the parent of this category.
     */
    public Category(String name, Category parent) {
        this.children = new ArrayList<>();
        this.items = new ArrayList<>();
        this.name = name;
        this.parent = parent;
    }

    /**
     * Adds a child category to the list of children.
     * @param child the child to be added.
     */
    public void addChild(Category child) {
        children.add(child);
        child.setParent(this);
    }

    /**
     * Adds an item to the list of items.
     * @param item the item to be added.
     */
    public void addItem(String item) {
        this.items.add(item);
    }

    /**
     * Returns the children of this category.
     * @return the children of this category.
     */
    public ArrayList<Category> getChildren() {
        return children;
    }

    /**
     * Returns the items of this category.
     * @return the items of this category.
     */
    public ArrayList<String> getItems() {
        return items;
    }

    /**
     * Returns the parent of this category.
     * @return the parent of this category.
     */
    public Category getParent() {
        return parent;
    }

    /**
     * Returns the name of this category.
     * @return the name of this category.
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the name of this category to a specified value.
     * @param name the name of this category.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Sets the parent of this category to a specified value.
     * @param parent the name of this category.
     */
    private void setParent(Category parent) {
        this.parent = parent;
    }

    /**
     * Returns the ID of this category.
     * @return the ID of this category.
     */
    public int getID() {
        return ID;
    }

    /**
     * Sets the ID of this category to a specified value.
     * @param ID the name of this category.
     */
    public void setID(int ID) {
        this.ID = ID;
    }
}

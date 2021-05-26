package com.unileipzig.shop;

import java.util.ArrayList;

public class Category {
    private String name;
    private ArrayList<Category> children;
    private Category parent;
    private ArrayList<String> items;
    private int ID;

    public Category() {
        this.children = new ArrayList<>();
        this.items = new ArrayList<>();
        this.name = null;
        this.parent = null;
    }

    public Category(String name) {
        this.children = new ArrayList<>();
        this.items = new ArrayList<>();
        this.name = name;
        this.parent = null;
    }

    public Category(String name, Category parent) {
        this.children = new ArrayList<>();
        this.items = new ArrayList<>();
        this.name = name;
        this.parent = parent;
    }

    public void addChild(Category child) {
        children.add(child);
        child.setParent(this);
    }

    public void addItem(String item) {
        this.items.add(item);
    }

    public ArrayList<Category> getChildren() {
        return children;
    }

    public ArrayList<String> getItems() {
        return items;
    }

    public Category getParent() {
        return parent;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    private void setParent(Category parent) {
        this.parent = parent;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }
}

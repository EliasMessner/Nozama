package com.unileipzig.shop;

public class Person {

    private int id;
    private String name;

    Person(int id, String name) {
        this.id = id;
        this.name = name;
    }

    Person(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Person)) return super.equals(o);
        return ((Person) o).getName().equals(name);
        // only compare name because when checking if contributors of a product are the same,
        // the ids of the parsed product are not yet set and are always different from those
        // in the database
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}

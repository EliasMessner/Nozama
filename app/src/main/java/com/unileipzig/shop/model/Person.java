package com.unileipzig.shop.model;

import static com.unileipzig.shop.CompareUtil.alphanumericallyEquals;

/**
 * model for table person
 */
public class Person {

    private int id;
    private String name;

    public Person(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public Person(String name) {
        this.name = name;
    }

    /**
     * @return true if the names are equal in regards to only alphanumerical characters
     */
    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Person)) return super.equals(o);
        return alphanumericallyEquals(((Person) o).getName(), name);
        // only compare name because when checking if contributors of a product are the same, the id of the parsed
        // product is not yet set and is always different from the one in the database
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

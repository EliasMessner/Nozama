package com.unileipzig.shop.model;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;

@Entity
@DynamicInsert
@DynamicUpdate
public class Customer {

    @Id
    private String username;

    private String firstName;

    private String lastName;

    private String bankAccount;

    private String street;

    @Column(length = 5)
    private String zip;

    public Customer() {}

    /**
     * Constructs a customer with a specified username.
     * @param username the username of the customer.
     */
    public Customer(String username) {
        this.username = username;
    }

    /**
     * Returns the username of this customer.
     * @return the username of this customer.
     */
    public String getUsername() {
        return username;
    }

    /**
     * Sets the username of this customer to a specified value.
     * @param username the username of this customer.
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * Returns the first name of this customer.
     * @return the first name of this customer.
     */
    public String getFirstName() {
        return firstName;
    }

    /**
     * Sets the first name of this customer to a specified value.
     * @param firstName the first name of this customer.
     */
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    /**
     * Returns the last name of this customer.
     * @return the last name of this customer.
     */
    public String getLastName() {
        return lastName;
    }

    /**
     * Sets the last name of this customer to a specified value.
     * @param lastName the last name of this customer.
     */
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    /**
     * Returns the bank account number of this customer.
     * @return the bank account number of this customer.
     */
    public String getBankAccount() {
        return bankAccount;
    }

    /**
     * Sets the bank account of this customer to a specified value.
     * @param bankAccount the bank account of this customer.
     */
    public void setBankAccount(String bankAccount) {
        this.bankAccount = bankAccount;
    }

    /**
     * Returns the street name of this customer.
     * @return the street name of this customer.
     */
    public String getStreet() {
        return street;
    }

    /**
     * Sets the street name of this customer to a specified value.
     * @param street the street name of this customer.
     */
    public void setStreet(String street) {
        this.street = street;
    }

    /**
     * Returns the zip code of this customer as a string.
     * @return the zip code of this customer.
     */
    public String getZip() {
        return zip;
    }

    /**
     * Sets the zip code of this customer to a specified value.
     * @param zip the zip code of this customer.
     */
    public void setZip(String zip) {
        this.zip = zip;
    }
}

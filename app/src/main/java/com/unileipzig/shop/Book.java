package com.unileipzig.shop;

import java.math.BigInteger;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Book extends Product {

    private int pageNumber;
    private LocalDate publicationDate;
    private String isbn;
    private List<String> publishers;
    private List<Person> authors;

    Book(String prodNumber, String title) {
        super(prodNumber, title);

        publishers = new ArrayList<>();
        authors = new ArrayList<>();
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Book)) {
            return super.equals(o);
        }
        Book other = (Book) o;
        return super.equals(other)
                && pageNumber == other.getPageNumber()
                && publicationDate.isEqual(other.getPublicationDate())
                && isbn == null || other.getIsbn() == null || isbn.equals(other.getIsbn())
                && publishers.containsAll(other.getPublishers()) && other.getPublishers().containsAll(publishers)
                && authors.containsAll(other.getAuthors()) && other.getAuthors().containsAll(authors);
    }

    public int getPageNumber() {
        return pageNumber;
    }

    public void setPageNumber(int pageNumber) {
        this.pageNumber = pageNumber;
    }

    public LocalDate getPublicationDate() {
        return publicationDate;
    }

    public void setPublicationDate(LocalDate publicationDate) {
        this.publicationDate = publicationDate;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public List<String> getPublishers() {
        return publishers;
    }

    public void setPublishers(List<String> publishers) {
        this.publishers = publishers;
    }

    public void addPublisher(String publisher) {
        this.publishers.add(publisher);
    }

    public List<Person> getAuthors() {
        return authors;
    }

    public void setAuthors(List<Person> authors) {
        this.authors = authors;
    }

    public void addAuthor(Person person) {
        authors.add(person);
    }
}

package com.unileipzig.shop;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static com.unileipzig.shop.CompareUtil.*;

/**
 * Model for table book.
 */
public class Book extends Product {

    private Integer pageNumber;
    private LocalDate publicationDate;
    private String isbn;
    private List<String> publishers;
    private List<Person> authors;

    /**
     * Constructs a book with specified product number and title. Publishers and Authors will be initialized as empty
     * lists of persons or strings respectively.
     * @param prodNumber The product number of this book.
     * @param title The title of this book.
     */
    Book(String prodNumber, String title) {
        super(prodNumber, title);
        publishers = new ArrayList<>();
        authors = new ArrayList<>();
    }

    /**
     * Returns true if this book is equal to the specified object. If the specified object is also a book,
     * As in product, the id will be ignored because it only serves as serial in the database, and all strings will
     * only be compared with regard to alphanumerical characters.
     * @param o the Object to compare this product to
     * @return true iff the above condition is satisfied.
     */
    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Book)) {
            return super.equals(o);
        }
        Book other = (Book) o;
        return super.equals(other)
                && equalsAllowNull(pageNumber, other.getPageNumber())
                && publicationDate.isEqual(other.getPublicationDate())
                && equalsAllowNull(isbn, other.getIsbn())
                && alphanumericallyEqualsIgnoreOrder(publishers, other.getPublishers())
                && authors.containsAll(other.getAuthors()) && other.getAuthors().containsAll(authors);
    }

    /**
     * Returns the page count of this book. May return null.
     * @return the page count of this book.
     */
    public Integer getPageNumber() {
        return pageNumber;
    }

    /**
     * Sets the page count of this book to a specified value. May be null.
     * @param pageNumber the page count of this book.
     */
    public void setPageNumber(Integer pageNumber) {
        this.pageNumber = pageNumber;
    }

    /**
     * Returns the publication date of this book.
     * @return the publication date of this book.
     */
    public LocalDate getPublicationDate() {
        return publicationDate;
    }

    /**
     * Sets the publication date of this book to a specified value. May be null.
     * @param publicationDate the publication date of this book.
     */
    public void setPublicationDate(LocalDate publicationDate) {
        this.publicationDate = publicationDate;
    }

    /**
     * Returns the isbn of this book.
     * @return the isbn of this book.
     */
    public String getIsbn() {
        return isbn;
    }

    /**
     * Sets the isbn of this book to a specified value.
     * @param isbn the isbn of this book.
     */
    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    /**
     * Returns a list of all publishers of the book, each represented by a string.
     * @return a list of all publishers of the book.
     */
    public List<String> getPublishers() {
        return publishers;
    }

    /**
     * Sets the publishers of this book to a specified list of strings.
     * @param publishers the publishers of this book.
     */
    public void setPublishers(List<String> publishers) {
        this.publishers = publishers;
    }

    /**
     * Adds a value to the list of publishers of this book.
     * @param publisher the string representing the publisher to be added.
     */
    public void addPublisher(String publisher) {
        this.publishers.add(publisher);
    }

    /**
     * Returns a list of all authors of the book, each represented by a Person object.
     * @return a list of all authors of the book.
     */
    public List<Person> getAuthors() {
        return authors;
    }

    /**
     * Sets the authors of this book to a specified list of Person objects.
     * @param authors the authors of this book.
     */
    public void setAuthors(List<Person> authors) {
        this.authors = authors;
    }

    /**
     * Adds a value to the list of authors of this book.
     * @param author the Person instance representing the author to be added.
     */
    public void addAuthor(Person author) {
        authors.add(author);
    }
}

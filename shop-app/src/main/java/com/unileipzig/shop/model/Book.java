package com.unileipzig.shop.model;

import com.vladmihalcea.hibernate.type.array.ListArrayType;
import org.hibernate.annotations.*;

import javax.persistence.*;
import javax.persistence.Entity;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@DynamicInsert
@DynamicUpdate
@TypeDef(
        name = "list-array",
        typeClass = ListArrayType.class
)
public class Book extends Product {

    private Integer pageNumber;

    private LocalDate publicationDate;

    @Column(unique = true)
    private String isbn;

    @Type(type = "list-array")
    @Column(name = "publishers", columnDefinition = "varchar[]")
    private List<String> publishers;

    @ManyToMany()
    @Cascade( { org.hibernate.annotations.CascadeType.SAVE_UPDATE })
    @JoinTable(name = "book_author", joinColumns = @JoinColumn(name = "book"), inverseJoinColumns = @JoinColumn(name =
    "author"))
    private List<Person> authors;

    public Book(){};

    /**
     * Constructs a book with specified product number and title. Publishers and Authors will be initialized as empty
     * lists of persons or strings respectively.
     * @param prodNumber The product number of this book.
     * @param title The title of this book.
     */
    public Book(String prodNumber, String title) {
        super(prodNumber, title);
        publishers = new ArrayList<>();
        authors = new ArrayList<>();
    }

    public Integer getPageNumber() {
        return pageNumber;
    }

    public void setPageNumber(Integer pageNumber) {
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

    public void addAuthor(Person author) {
        authors.add(author);
    }
}

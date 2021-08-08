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
public class MusicCd extends Product {

    @Type(type = "list-array")
    @Column(name = "labels", columnDefinition = "varchar[]")
    private List<String> labels;

    private LocalDate publicationDate;

    @Type(type = "list-array")
    @Column(name = "titles", columnDefinition = "varchar[]")
    private List<String> titles;

    @ManyToMany()
    @Cascade( { org.hibernate.annotations.CascadeType.SAVE_UPDATE })
    @JoinTable(name = "cd_artist", joinColumns = @JoinColumn(name = "cd"), inverseJoinColumns = @JoinColumn(name =
            "artist"))
    private List<Person> artists;

    public MusicCd(){};

    /**
     * Constructs a MusicCd with a specified product number and title. Artists, labels and titles will be initialized as
     * empty lists of persons or strings respectively.
     * @param prodNumber the product number of this music cd.
     * @param title the title of this music cd.
     */
    public MusicCd(String prodNumber, String title) {
        super(prodNumber, title);
        labels = new ArrayList<>();
        titles = new ArrayList<>();
        artists = new ArrayList<>();
    }

    public List<String> getLabels() {
        return labels;
    }

    public void setLabels(List<String> labels) {
        this.labels = labels;
    }

    public void addLabel(String label) {
        this.labels.add(label);
    }

    public LocalDate getPublicationDate() {
        return publicationDate;
    }

    public void setPublicationDate(LocalDate publicationDate) {
        this.publicationDate = publicationDate;
    }

    public List<String> getTitles() {
        return titles;
    }

    public void setTitles(List<String> titles) {
        this.titles = titles;
    }

    public void addTitle(String title) {
        this.titles.add(title);
    }

    public List<Person> getArtists() {
        return artists;
    }

    public void setArtists(List<Person> artists) {
        this.artists = artists;
    }

    public void addArtist(Person artist) {
        this.artists.add(artist);
    }

    @Override
    public String toString() {
        return String.format(super.toString() +
                "Product Type: Music-CD\n" +
                "Publication Date: %s\n",
                publicationDate);
    }
}

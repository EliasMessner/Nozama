package com.unileipzig.shop;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MusicCd extends Product {

    private List<String> labels;
    private LocalDate publicationDate;
    private List<String> titles;
    private List<Person> artists;

    MusicCd(String prodNumber, String title) {
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

    public void addLabel(String label) {
        this.labels.add(label);
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
}

package com.unileipzig.shop;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static com.unileipzig.shop.CompareUtil.alphanumericallyEqualsIgnoreOrder;
import static com.unileipzig.shop.CompareUtil.equalsAllowNull;

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

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof MusicCd)) {
            return super.equals(o);
        }
        MusicCd other = (MusicCd) o;
        return super.equals(other)
                && alphanumericallyEqualsIgnoreOrder(labels, other.getLabels())
                && equalsAllowNull(publicationDate, other.getPublicationDate())
                && alphanumericallyEqualsIgnoreOrder(titles, other.getTitles())
                && artists.containsAll(other.getArtists()) && other.getArtists().containsAll(artists);
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

    public void addArtist(Person person) {
        artists.add(person);
    }
}

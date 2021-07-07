package com.unileipzig.shop;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import static com.unileipzig.shop.CompareUtil.alphanumericallyEqualsIgnoreOrder;
import static com.unileipzig.shop.CompareUtil.equalsAllowNull;

/**
 * Model for table Music_cd.
 */
public class MusicCd extends Product {

    private List<String> labels;
    private LocalDate publicationDate;
    private List<String> titles;
    private List<Person> artists;

    /**
     * Constructs a MusicCd with a specified product number and title. Artists, labels and titles will be initialized as
     * empty lists of persons or strings respectively.
     * @param prodNumber the product number of this music cd.
     * @param title the title of this music cd.
     */
    MusicCd(String prodNumber, String title) {
        super(prodNumber, title);
        labels = new ArrayList<>();
        titles = new ArrayList<>();
        artists = new ArrayList<>();
    }

    /**
     * Returns true if this MusicCd is equal to the specified object. If the specified object is also a MusicCd,
     * As in product, the id will be ignored because it only serves as serial in the database, and all strings will
     * only be compared with regard to alphanumerical characters.
     * @param o the Object to compare this product to
     * @return true iff the above condition is satisfied.
     */
    @Override
    public boolean equals(Object o) {
        if (!(o instanceof MusicCd)) {
            return super.equals(o);
        }
        MusicCd other = (MusicCd) o;
        return super.equals(other)
                && alphanumericallyEqualsIgnoreOrder(labels, other.getLabels())
                && equalsAllowNull(publicationDate, other.getPublicationDate())
                && titles == null || other.getTitles() == null || alphanumericallyEqualsIgnoreOrder(titles, other.getTitles())
                && artists.containsAll(other.getArtists()) && other.getArtists().containsAll(artists);
    }

    /**
     * Returns the labels of this music-cd.
     * @return the labels of this music-cd.
     */
    public List<String> getLabels() {
        return labels;
    }

    /**
     * Sets the labels of this music-cd to a specified value.
     * @param labels the labels of this music-cd.
     */
    public void setLabels(List<String> labels) {
        this.labels = labels;
    }

    /**
     * Returns the publication date of this music-cd.
     * @return the publication date of this music-cd.
     */
    public LocalDate getPublicationDate() {
        return publicationDate;
    }

    /**
     * Sets the publication date of this music-cd to a specified value.
     * @param publicationDate the publication date of this music-cd.
     */
    public void setPublicationDate(LocalDate publicationDate) {
        this.publicationDate = publicationDate;
    }

    /**
     * Returns the titles of this music-cd.
     * @return the titles of this music-cd.
     */
    public List<String> getTitles() {
        return titles;
    }

    /**
     * Sets the titles of this music-cd to a specified value.
     * @param titles the titles of this music-cd.
     */
    public void setTitles(List<String> titles) {
        this.titles = titles;
    }

    /**
     * Adds a value to the list of labels of this music-cd.
     * @param label the label to be added.
     */
    public void addLabel(String label) {
        this.labels.add(label);
    }

    /**
     * Adds a value to the list of titles. of this music-cd.
     * @param title the title to be added.
     */
    public void addTitle(String title) {
        this.titles.add(title);
    }

    /**
     * Returns the artists of this music-cd.
     * @return the artists of this music-cd.
     */
    public List<Person> getArtists() {
        return artists;
    }

    /**
     * Sets the artists of this music-cd to a specified value.
     * @param artists the artists of this music-cd.
     */
    public void setArtists(List<Person> artists) {
        this.artists = artists;
    }

    /**
     * Adds a value to the list of persons of this music-cd.
     * @param person the person to be added.
     */
    public void addArtist(Person person) {
        artists.add(person);
    }
}

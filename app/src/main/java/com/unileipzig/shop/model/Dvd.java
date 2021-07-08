package com.unileipzig.shop.model;

import java.util.ArrayList;
import java.util.List;

import static com.unileipzig.shop.CompareUtil.equalsAllowNull;

/**
 * Model for table DVD.
 */
public class Dvd extends Product {

    private String format;
    private Integer durationMinutes;
    private short regionCode;
    private List<Person> actors;
    private List<Person> creators;
    private List<Person> directors;

    /**
     * Constructs a DVD Object with empty lists for all person-lists, initial rating of 3.0, specified product number
     * and title. actors, creators and directors will be initialized as empty lists of persons.
     * @param prodNumber
     * @param title
     */
    public Dvd(String prodNumber, String title) {
        super(prodNumber, title);
        actors = new ArrayList<>();
        creators = new ArrayList<>();
        directors = new ArrayList<>();
    }

    /**
     * Returns true if this dvd is equal to the specified object. If the specified object is also a dvd,
     * As in product, the id will be ignored because it only serves as serial in the database, and all strings will
     * only be compared with regard to alphanumerical characters.
     * @param o the Object to compare this product to
     * @return true iff the above condition is satisfied.
     */
    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Dvd)) {
            return super.equals(o);
        }
        Dvd other = (Dvd) o;
        return super.equals(other)
                && equalsAllowNull(format, other.getFormat())
                && equalsAllowNull(durationMinutes, other.getDurationMinutes())
                && regionCode == other.getRegionCode()
                && actors.containsAll(other.getActors()) && other.getActors().containsAll(actors)
                && creators.containsAll(other.getCreators()) && other.getCreators().containsAll(creators)
                && directors.containsAll(other.getDirectors()) && other.getDirectors().containsAll(directors);
    }

    /**
     * Returns the format of this dvd.
     * @return the format of this dvd.
     */
    public String getFormat() {
        return format;
    }

    /**
     * Sets the format of this dvd to a specified value.
     * @param format the format of this dvd.
     */
    public void setFormat(String format) {
        this.format = format;
    }

    /**
     * Returns the duration of this dvd in minutes.
     * @return the duration of this dvd in minuts.
     */
    public Integer getDurationMinutes() {
        return durationMinutes;
    }

    /**
     * Sets the duration of this dvd to a specified value in minutes.
     * @param durationMinutes the duration of this dvd in minutes.
     */
    public void setDurationMinutes(Integer durationMinutes) {
        this.durationMinutes = durationMinutes;
    }

    /**
     * Returns the region code of this dvd.
     * @return the region code of this dvd.
     */
    public short getRegionCode() {
        return regionCode;
    }

    /**
     * Sets the region code of this dvd to a specified value.
     * @param regionCode the region code of this dvd.
     */
    public void setRegionCode(short regionCode) {
        this.regionCode = regionCode;
    }

    /**
     * Returns a list of the actors of this dvd.
     * @return the actors of this dvd.
     */
    public List<Person> getActors() {
        return actors;
    }

    /**
     * Sets the actors of this dvd to a specified list.
     * @param actors the actors of this dvd.
     */
    public void setActors(List<Person> actors) {
        this.actors = actors;
    }

    /**
     * Returns a list of the creators of this dvd.
     * @return the creators of this dvd.
     */
    public List<Person> getCreators() {
        return creators;
    }

    /**
     * Sets the creators of this dvd to a specified list.
     * @param creators the creators of this dvd.
     */
    public void setCreators(List<Person> creators) {
        this.creators = creators;
    }

    /**
     * Returns a list of the directors of this dvd.
     * @return the directors of this dvd.
     */
    public List<Person> getDirectors() {
        return directors;
    }

    /**
     * Sets the directors of this dvd to a specified list.
     * @param directors the directors of this dvd.
     */
    public void setDirectors(List<Person> directors) {
        this.directors = directors;
    }

    /**
     * Adds an Person to the list of actors of this dvd.
     * @param person the Person to be added.
     */
    public void addActor(Person person) {
        actors.add(person);
    }

    /**
     * Adds an Person to the list of creators of this dvd.
     * @param person the Person to be added.
     */
    public void addCreator(Person person) {
        creators.add(person);
    }

    /**
     * Adds an Person to the list of directors of this dvd.
     * @param person the Person to be added.
     */
    public void addDirector(Person person) {
        directors.add(person);
    }
}

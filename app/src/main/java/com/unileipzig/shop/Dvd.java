package com.unileipzig.shop;

import java.util.ArrayList;
import java.util.List;

import static com.unileipzig.shop.CompareUtil.equalsAllowNull;

/**
 * Represents a DVD.
 */
public class Dvd extends Product {

    private String format;
    private Integer durationMinutes;
    private short regionCode;
    private List<Person> actors;
    private List<Person> creators;
    private List<Person> directors;

    /**
     * Constructs a DVD Object with empty lists for all person-lists, initial rating of 3.0, specified product number and title.
     * @param prodNumber
     * @param title
     */
    Dvd(String prodNumber, String title) {
        super(prodNumber, title);
        actors = new ArrayList<>();
        creators = new ArrayList<>();
        directors = new ArrayList<>();
    }

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

    public String getFormat() {
        return format;
    }

    public void setFormat(String format) {
        this.format = format;
    }

    public Integer getDurationMinutes() {
        return durationMinutes;
    }

    public void setDurationMinutes(Integer durationMinutes) {
        this.durationMinutes = durationMinutes;
    }

    public short getRegionCode() {
        return regionCode;
    }

    public void setRegionCode(short regionCode) {
        this.regionCode = regionCode;
    }

    public List<Person> getActors() {
        return actors;
    }

    public void setActors(List<Person> actors) {
        this.actors = actors;
    }

    public List<Person> getCreators() {
        return creators;
    }

    public void setCreators(List<Person> creators) {
        this.creators = creators;
    }

    public List<Person> getDirectors() {
        return directors;
    }

    public void setDirectors(List<Person> directors) {
        this.directors = directors;
    }

    public void addActor(Person person) {
        actors.add(person);
    }

    public void addCreator(Person person) {
        creators.add(person);
    }

    public void addDirector(Person person) {
        directors.add(person);
    }
}

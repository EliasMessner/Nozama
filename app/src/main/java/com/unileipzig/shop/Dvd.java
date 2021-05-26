package com.unileipzig.shop;

import java.util.ArrayList;
import java.util.List;

public class Dvd extends Product {

    private String format;
    private int durationMinutes;
    private short regionCode;
    private List<Person> actors;
    private List<Person> creators;
    private List<Person> directors;

    Dvd(String prodNumber, String title) {
        super(prodNumber, title);

        actors = new ArrayList<>();
        creators = new ArrayList<>();
        directors = new ArrayList<>();
    }

    public String getFormat() {
        return format;
    }

    public void setFormat(String format) {
        this.format = format;
    }

    public int getDurationMinutes() {
        return durationMinutes;
    }

    public void setDurationMinutes(int durationMinutes) {
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
}

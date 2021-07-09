package com.unileipzig.shop.model;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import java.util.ArrayList;
import java.util.List;

@Entity
@DynamicInsert
@DynamicUpdate
public class Dvd extends Product {

    private String format;

    private Integer durationMinutes;

    private short regionCode;

    @ManyToMany()
    @Cascade( { org.hibernate.annotations.CascadeType.SAVE_UPDATE })
    @JoinTable(name = "dvd_actor", joinColumns = @JoinColumn(name = "dvd"), inverseJoinColumns = @JoinColumn(name =
            "actor"))
    private List<Person> actors;

    @ManyToMany()
    @Cascade( { org.hibernate.annotations.CascadeType.SAVE_UPDATE })
    @JoinTable(name = "dvd_creator", joinColumns = @JoinColumn(name = "dvd"), inverseJoinColumns = @JoinColumn(name =
            "creator"))
    private List<Person> creators;

    @ManyToMany()
    @Cascade( { org.hibernate.annotations.CascadeType.SAVE_UPDATE })
    @JoinTable(name = "dvd_director", joinColumns = @JoinColumn(name = "dvd"), inverseJoinColumns = @JoinColumn(name =
            "director"))
    private List<Person> directors;

    public Dvd(){};

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

    public void addActor(Person actor) {
        this.actors.add(actor);
    }

    public List<Person> getCreators() {
        return creators;
    }

    public void setCreators(List<Person> creators) {
        this.creators = creators;
    }

    public void addCreator(Person creator) {
        this.creators.add(creator);
    }

    public List<Person> getDirectors() {
        return directors;
    }

    public void setDirectors(List<Person> directors) {
        this.directors = directors;
    }

    public void addDirector(Person director) {
        this.directors.add(director);
    }
}

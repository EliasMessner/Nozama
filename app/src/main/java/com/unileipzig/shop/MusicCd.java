package com.unileipzig.shop;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MusicCd extends Product {

    private List<String> labels;
    private LocalDate publicationDate;
    private List<String> titles;

    MusicCd(String prodNumber, String title) {
        super(prodNumber, title);

        labels = new ArrayList<>();
        titles = new ArrayList<>();
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
}

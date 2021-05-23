package com.unileipzig.shop;

import java.util.Date;

public class MusicCd extends Product {

    private String label;
    private Date publicationDate;
    private String[] titles;

    MusicCd(int prodNumber, String title) {
        super(prodNumber, title);
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public Date getPublicationDate() {
        return publicationDate;
    }

    public void setPublicationDate(Date publicationDate) {
        this.publicationDate = publicationDate;
    }

    public String[] getTitles() {
        return titles;
    }

    public void setTitles(String[] titles) {
        this.titles = titles;
    }
}

package org.example.domain;

import java.io.Serializable;

public class CD implements Serializable {
    private static final long serialVersionUID = 1L;
    private String title;
    private String artist;
    private String id;

    public CD(String title, String artist, String id) {
        this.title = title;
        this.artist = artist;
        this.id = id;
    }

    public String getTitle() { return title; }
    public String getArtist() { return artist; }
    public String getId() { return id; }

    @Override
    public String toString() {
        return title + " by " + artist + " (ID: " + id + ")";
    }
}

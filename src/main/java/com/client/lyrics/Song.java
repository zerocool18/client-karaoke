package com.client.lyrics;

import lombok.Data;
import org.codehaus.jackson.annotate.JsonProperty;

import java.util.List;

@Data
public class Song {

    private Long id;
    private String title;
    private String artist;
    private String album;
    private String cover;
    private List<Sentence> sentences;
    @JsonProperty("lyrics")
    private Lyric lyrics;

    private double length;

    public Song(){
        super();
    }
    public Song(String title, String artist, String album, String cover, Lyric lyrics, double length) {
        this.title = title;
        this.artist = artist;
        this.album = album;
        this.cover = cover;
        this.lyrics = lyrics;
        this.length = length;
    }
}

package com.example.mediaplayer;

import java.io.Serializable;

public class Song implements Serializable {
    String songName;
    String artistName;
    String link;
    String photoID;

    public Song(String songName, String artistName, String link, String photoID) {
        this.songName = songName;
        this.artistName = artistName;
        this.link = link;
        this.photoID = photoID;
    }

    public String getSongName() {
        return songName;
    }

    public void setSongName(String songName) {
        this.songName = songName;
    }

    public String getArtistName() {
        return artistName;
    }

    public void setArtistName(String artistName) {
        this.artistName = artistName;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getPhotoID() {
        return photoID;
    }

    public void setPhotoID(String photoID) {
        this.photoID = photoID;
    }

    @Override
    public String toString() {
        return "Song{" +
                "songName='" + songName + '\'' +
                ", artistName='" + artistName + '\'' +
                ", link='" + link + '\'' +
                ", photoID=" + photoID +
                '}';
    }

}

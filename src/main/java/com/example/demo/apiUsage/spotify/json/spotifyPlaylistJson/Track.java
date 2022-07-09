package com.example.demo.apiUsage.spotify.json.spotifyPlaylistJson;

import com.google.gson.annotations.SerializedName;

public class Track {
    @SerializedName("album")
    public Album album;
    @SerializedName("name")
    public String name;

    @Override
    public String toString() {
        return "Track{" +
                "album=" + album +
                ", name='" + name + '\'' +
                '}';
    }
}

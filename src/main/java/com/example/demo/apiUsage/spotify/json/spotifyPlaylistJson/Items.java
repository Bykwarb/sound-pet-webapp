package com.example.demo.apiUsage.spotify.json.spotifyPlaylistJson;

import com.google.gson.annotations.SerializedName;

public class Items {
    @SerializedName("track")
    public Track track;

    @Override
    public String toString() {
        return "Items{" +
                "track=" + track +
                '}';
    }
}

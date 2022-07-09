package com.example.demo.apiUsage.spotify.json.spotifyPlaylistJson;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class SpotifyPlayListResponseJson {
    @SerializedName("items")
    public List<Items> items;

    @Override
    public String toString() {
        return "ResponseJson{" +
                "items=" + items +
                '}';
    }


}

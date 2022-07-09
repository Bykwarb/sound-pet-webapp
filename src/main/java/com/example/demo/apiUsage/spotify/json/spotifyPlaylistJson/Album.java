package com.example.demo.apiUsage.spotify.json.spotifyPlaylistJson;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class Album {
    @SerializedName("artists")
    public List<Artists> artists;

    public List<String> artistsName(){
        List<String> list = new ArrayList<>();
        for (Artists artists1 : artists){
            list.add(artists1.name);
        }
        return list;
    }

    @Override
    public String toString() {
        return "Album{" +
                "artists=" + artists +
                '}';
    }
}

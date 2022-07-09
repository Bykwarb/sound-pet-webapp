package com.example.demo.apiUsage.spotify.json.spotifyAlbumJson;

import java.util.ArrayList;
import java.util.List;

public class Items {
    public List<Artists> artists;
    public String disc_number;
    public String duration_ms;
    public String explicit;
    public External_urls external_urls;
    public String href;
    public String id;
    public String is_local;
    public String is_playable;
    public String name;
    public String preview_url;
    public String track_number;
    public String type;
    public String uri;

    public List<String> artistsName(){
        List<String> list = new ArrayList<>();
        for (Artists artists1: artists){
            list.add(artists1.name);
        }
        return list;
    }
}

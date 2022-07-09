package com.example.demo.apiUsage.spotify.json.spotifyPlaylistJson;

import com.google.gson.annotations.SerializedName;

public class Artists {
    @SerializedName("external_urls")
    public External_urls external_urls;
    @SerializedName("href")
    public String href;
    @SerializedName("id")
    public String id;
    @SerializedName("name")
    public String name;
    @SerializedName("type")
    public String type;
    @SerializedName("uri")
    public String uri;

    public External_urls getExternal_urls() {
        return external_urls;
    }

    public void setExternal_urls(External_urls external_urls) {
        this.external_urls = external_urls;
    }

    public String getHref() {
        return href;
    }

    public void setHref(String href) {
        this.href = href;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    @Override
    public String toString() {
        return "Artists{" +
                "external_urls=" + external_urls +
                ", href='" + href + '\'' +
                ", id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", type='" + type + '\'' +
                ", uri='" + uri + '\'' +
                '}';
    }
}

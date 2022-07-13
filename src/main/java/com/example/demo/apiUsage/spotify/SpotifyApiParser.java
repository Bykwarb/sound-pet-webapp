package com.example.demo.apiUsage.spotify;

import com.example.demo.apiUsage.spotify.json.spotifyAlbumJson.SpotifyAlbumResponseJson;
import com.example.demo.apiUsage.spotify.json.spotifyPlaylistJson.Items;
import com.example.demo.apiUsage.spotify.json.spotifyPlaylistJson.SpotifyPlayListResponseJson;
import com.example.demo.apiUsage.spotify.json.spotifyToken.TokenResponse;

import com.example.demo.exceptions.NotCorrectUrlException;
import org.apache.http.client.HttpResponseException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Component
@PropertySource("classpath:spotify.properties")
public class SpotifyApiParser {

    private String url;

    private StringBuffer playlistUrl;

    private StringBuffer albumUrl;

    private String token;

    private Map<String, List<String>> songList;

    private static final HttpClient client = HttpClient.newHttpClient();
    //Private data which is needed for getting spotify token. Read more in official spotify.api documentation
    @Value("${client.data}")
    private String spotifyClientData;

    public static SpotifyApiParser build(){
        return new SpotifyApiParser();
    }

    //Parse Spotify Api to get the name of the songs in the playlist
    public void parse() throws IOException, InterruptedException{
        parseToken();
        createUrl();
        HttpRequest request = HttpRequest.newBuilder(
                URI.create(url))
                .headers("Accept", "application/json", "Content-Type", "application/json", "Authorization", "Bearer " + token).build();
        HttpResponse<SpotifyPlayListResponseJson> response = client.send(request, new JsonBodyHandler<>(SpotifyPlayListResponseJson.class));
        if (response.statusCode() != 200){
            throw new HttpResponseException(response.statusCode(), "Not correct url");
        }
        SpotifyPlayListResponseJson item = response.body();
        songList = new LinkedHashMap<>();
        for (Items items : item.items){
            songList.put(items.track.name, items.track.album.artistsName());
        }
    }

    //Parse Spotify Api to get the name of the songs on the album
    public void parseAlbum() throws IOException, InterruptedException {
        parseToken();
        createAlbumUrl();
        HttpRequest request = HttpRequest.newBuilder(
                        URI.create(url))
                .headers("Accept", "application/json", "Content-Type", "application/json", "Authorization", "Bearer " + token).build();
        HttpResponse<SpotifyAlbumResponseJson> response = client.send(request, new JsonBodyHandler<>(SpotifyAlbumResponseJson.class));
        if (response.statusCode() != 200){
            throw new HttpResponseException(response.statusCode(), "Not correct url");
        }
        SpotifyAlbumResponseJson it = response.body();
        songList = new LinkedHashMap<>();
        for (com.example.demo.apiUsage.spotify.json.spotifyAlbumJson.Items items : it.items){
            songList.put(items.name, items.artistsName());
        }

    }

    //Parse api verification token from spotify api
    private void parseToken() throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder(URI.create("https://accounts.spotify.com/api/token?grant_type=client_credentials")).
                headers("Authorization", "Basic " + spotifyClientData,
                        "Content-Type", "application/x-www-form-urlencoded").POST(HttpRequest.BodyPublishers.noBody()).build();
        HttpResponse<TokenResponse> response = client.send(request, new JsonBodyHandler<>(TokenResponse.class));
        if (response.statusCode() != 200){
            throw new HttpResponseException(response.statusCode(), "Not valid spotify client data");
        }
        TokenResponse it = response.body();
        token = it.access_token;
    }

    //Rework playlist url to correct
    private void createUrl(){
        if (Objects.isNull(playlistUrl)){
            throw new NullPointerException("PlaylistUrl is null");
        }
        else if (playlistUrl.length() < 31) {
            throw new NotCorrectUrlException("Not valid playlistUrl");
        }
        playlistUrl.delete(0,34);
        if(playlistUrl.indexOf("?") != -1){
            playlistUrl.delete(playlistUrl.indexOf("?"),playlistUrl.length());
        }
        url = "https://api.spotify.com/v1/playlists/" + playlistUrl + "/tracks?market=UA&fields=items(track(name%2Calbum(artists(name))))";
    }

    //Rework album url to correct
    private void createAlbumUrl(){
        if (Objects.isNull(albumUrl)){
            throw new NullPointerException("AlbumUrl is null");
        }
        else if (albumUrl.length() < 31) {
            throw new NotCorrectUrlException("Not valid albumUrl");
        }
        albumUrl.delete(0,31);
        if (albumUrl.indexOf("?") != -1) {
            albumUrl.delete(albumUrl.indexOf("?"), albumUrl.length());
        }
        url = "https://api.spotify.com/v1/albums/" + albumUrl + "/tracks?market=UA";

    }
    public Map<String, List<String>> getSongList() {
        return songList;
    }


    public StringBuffer getPlaylistUrl() {
        return playlistUrl;
    }

    public void setPlaylistUrl(StringBuffer playlistUrl) {
        this.playlistUrl = playlistUrl;
    }

    public StringBuffer getAlbumUrl() {
        return albumUrl;
    }

    public void setAlbumUrl(StringBuffer albumUrl){
        this.albumUrl = albumUrl;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getUrl() {
        return url;
    }

    public String getToken() {
        return token;
    }

    public void setSpotifyClientData(String spotifyClientData) {
        this.spotifyClientData = spotifyClientData;
    }

    public void setSongList(Map<String, List<String>> songList) {
        this.songList = songList;
    }
}

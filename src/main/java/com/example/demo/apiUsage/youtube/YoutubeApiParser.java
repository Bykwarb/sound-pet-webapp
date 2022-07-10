package com.example.demo.apiUsage.youtube;

import com.example.demo.apiUsage.spotify.JsonBodyHandler;
import com.example.demo.apiUsage.spotify.SpotifyApiParser;
import com.example.demo.apiUsage.youtube.json.Items;
import com.example.demo.apiUsage.youtube.json.YoutubeResponseJson;
import com.example.demo.entity.YtTokenEntity;
import com.example.demo.service.YtTokenService;
import com.example.demo.service.YtTokenServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;

@Component
public class YoutubeApiParser {

    private String url;

    private String token;

    private List<String> keywords;

    private List<String> youtubeUrls;

    private final SpotifyApiParser spotify;

    private final String[] headers = {"Accept", "application/json"};

    private static final HttpClient client = HttpClient.newHttpClient();

    private final YtTokenService tokenService;

    private YtTokenEntity ytTokenEntity;

    private List<YtTokenEntity> ytTokenEntities;

    public YoutubeApiParser(SpotifyApiParser spotify, YtTokenService tokenService) {
        this.spotify = spotify;
        this.tokenService = tokenService;
    }

    //Parse Google Youtube api to get the name of the songs from spotifyApiParser songlist
    public void multiThreadParse() throws InterruptedException {
        //Every request to Google Api cost a certain amount of quota. Everyday limit - 10000 quota.
        //Each request in this class cost 100 quotas.
        //Therefore, a database was created with access tokens, each of which has 10,000 quotas.
        // When a token runs out of quota, it becomes disabled. And after 24 hours the program resets this state again
        ytTokenEntities = tokenService.getTokens();
        System.out.println(ytTokenEntities.size());
        createKeywords();
        youtubeUrls = new ArrayList<>();
        for (int urlNumber = 0; urlNumber < keywords.size(); urlNumber++){
            youtubeUrls.add(null);
        }
        Thread even = new Thread(()->{
            for (int urlNumber = 0; urlNumber < keywords.size(); urlNumber++){
                if (urlNumber%2==0){
                    HttpResponse<YoutubeResponseJson> response = null;
                    while (response == null){
                        createUrl(urlNumber);
                        HttpRequest request = HttpRequest.newBuilder(URI.create(url))
                                .headers(headers).build();
                        try {
                            response = client.send(request, new JsonBodyHandler<>(YoutubeResponseJson.class));
                        } catch (IOException | InterruptedException ex) {
                            throw new RuntimeException(ex);
                        }
                        if (response.statusCode() !=200){
                            //Set token disabled
                            synchronized (this){
                                ytTokenEntities.get(0).setQuota(false);
                            }
                            tokenService.save(ytTokenEntities.remove(0));
                            response = null;
                        }
                    }
                    YoutubeResponseJson item = response.body();
                    synchronized (this){
                        for (Items items : item.items){
                            youtubeUrls.set(urlNumber, "https://www.youtube.com/watch?v=" + items.id.videoId);
                        }
                    }
                }
            }

        });
        Thread uneven = new Thread(()->{
            for (int urlNumber = 0; urlNumber < keywords.size(); urlNumber++){
                if (urlNumber%2!=0){
                    HttpResponse<YoutubeResponseJson> response = null;
                    while (response == null){
                    createUrl(urlNumber);
                    System.out.println(token);
                    HttpRequest request = HttpRequest.newBuilder(URI.create(url))
                            .headers(headers).build();
                        try {
                            response = client.send(request, new JsonBodyHandler<>(YoutubeResponseJson.class));
                        } catch (IOException | InterruptedException ex) {
                            throw new RuntimeException(ex);
                        }
                        if (response.statusCode() !=200){
                            synchronized (this){
                                ytTokenEntities.get(0).setQuota(false);
                            }
                            tokenService.save(ytTokenEntities.remove(0));
                            response = null;
                        }
                    }
                    YoutubeResponseJson item = response.body();
                    synchronized (this){
                        for (Items items : item.items){
                            youtubeUrls.set(urlNumber, "https://www.youtube.com/watch?v=" + items.id.videoId);
                        }
                    }
                }
            }
        });
        even.start();
        uneven.start();
        even.join();
        uneven.join();

    }

    //The query uses the title and author of the song as keywords.
    private void createKeywords(){
        keywords = new ArrayList<>();
        spotify.getSongList().forEach((x,y)-> {
            String song = (x + " " + y).replace("[", "").replace("]","");
            keywords.add(song);
        });
    }
    //Create query url with keyword in him
    private synchronized void createUrl(int keywordNumber){
        synchronized (this){
            ytTokenEntity = ytTokenEntities.get(0);
        }
        token = ytTokenEntity.getToken();
        System.out.println(token);
        url = "https://youtube.googleapis.com/youtube/v3/search?maxResults=1&q="
                + keywords.get(keywordNumber).replace(" ", "%25") + "&key=" + token;
    }
    public void setToken(String token) {
        this.token = token;
    }

    public List<String> getKeywords() {
        return keywords;
    }

    public String getUrl() {
        return url;
    }

    public String getToken() {
        return token;
    }

    public List<String> getYoutubeUrls() {
        return youtubeUrls;
    }

    public SpotifyApiParser getSpotify() {
        return spotify;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setKeywords(List<String> keywords) {
        this.keywords = keywords;
    }

    public void setYoutubeUrls(List<String> youtubeUrls) {
        this.youtubeUrls = youtubeUrls;
    }
}
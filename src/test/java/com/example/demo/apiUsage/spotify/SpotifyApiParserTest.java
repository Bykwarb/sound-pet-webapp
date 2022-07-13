package com.example.demo.apiUsage.spotify;

import com.example.demo.exceptions.NotCorrectUrlException;
import org.apache.http.client.HttpResponseException;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;

@RunWith(SpringRunner.class)
@SpringBootTest
@TestPropertySource("classpath:spotify.properties")
class SpotifyApiParserTest {
    @Value("${client.data}")
    String data;


    @Test
    void parse() throws IOException, InterruptedException {
        int playlistSize = 6;
        SpotifyApiParser apiParser = new SpotifyApiParser();
        apiParser.setPlaylistUrl(new StringBuffer("https://open.spotify.com/playlist/4sPrr4u7LCB9RINhvMvfsb"));
        apiParser.setSpotifyClientData(data);
        Assert.assertNull(apiParser.getSongList());
        apiParser.parse();
        Assert.assertEquals(playlistSize, apiParser.getSongList().size());
    }

    @Test
    void parseAlbum() throws IOException, InterruptedException {
        int playlistSize = 20;
        SpotifyApiParser apiParser = new SpotifyApiParser();
        apiParser.setAlbumUrl(new StringBuffer("https://open.spotify.com/album/0vshXZYhBkbIoqxyC2fXcF"));
        apiParser.setSpotifyClientData(data);
        Assert.assertNull(apiParser.getSongList());
        apiParser.parseAlbum();
        Assert.assertEquals(playlistSize, apiParser.getSongList().size());
    }

    @Test
    void parseWithoutSpotifyData() throws IOException, InterruptedException {
        SpotifyApiParser apiParser = new SpotifyApiParser();
        Assert.assertThrows(HttpResponseException.class, apiParser::parse);
    }
    @Test
    void parseWithoutPlaylistUrl() throws IOException, InterruptedException {
        SpotifyApiParser apiParser = new SpotifyApiParser();
        apiParser.setSpotifyClientData(data);
        Assert.assertThrows(NullPointerException.class, apiParser::parse);
        Assert.assertThrows(NullPointerException.class, apiParser::parseAlbum);
    }
    @Test
    void parseWithoutCorrectUrl() throws IOException, InterruptedException {
        SpotifyApiParser apiParser = new SpotifyApiParser();
        apiParser.setSpotifyClientData(data);
        apiParser.setPlaylistUrl(new StringBuffer("somethingData"));
        apiParser.setAlbumUrl(new StringBuffer("somethingData"));
        Assert.assertThrows(NotCorrectUrlException.class, apiParser::parse);
        Assert.assertThrows(NotCorrectUrlException.class, apiParser::parseAlbum);
    }
    @Test
    void parseWithBrokenLink(){
        SpotifyApiParser apiParser = new SpotifyApiParser();
        apiParser.setSpotifyClientData(data);
        apiParser.setPlaylistUrl(new StringBuffer("https://open.spotify.com/playlist/4sPrr4u7vMvfsb"));
        apiParser.setAlbumUrl(new StringBuffer("https://open.spotify.com/album/0vshXZYhBkbI"));
        Assert.assertThrows(HttpResponseException.class, apiParser::parse);
        Assert.assertThrows(HttpResponseException.class, apiParser::parseAlbum);
    }

}
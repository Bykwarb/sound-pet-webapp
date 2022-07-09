package com.example.demo.apiUsage.spotify;

import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.util.Properties;

import static org.junit.jupiter.api.Assertions.*;
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
        System.out.println();
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
        System.out.println();
        Assert.assertNull(apiParser.getSongList());
        apiParser.parseAlbum();
        System.out.println(apiParser.getSongList());
        Assert.assertEquals(playlistSize, apiParser.getSongList().size());
    }

}
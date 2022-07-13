package com.example.demo.apiUsage.youtube;

import com.example.demo.apiUsage.spotify.SpotifyApiParser;
import com.example.demo.entity.YtTokenEntity;
import com.example.demo.repository.YtTokenRepository;
import com.example.demo.service.YtTokenService;
import org.junit.Assert;
import org.junit.internal.builders.NullBuilder;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import java.lang.reflect.UndeclaredThrowableException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@RunWith(SpringRunner.class)
@SpringBootTest
class YoutubeApiParserTest {

    private static Map<String, List<String>> tracklist = new LinkedHashMap<>();

    private final static List<String> urlList = List.of("https://www.youtube.com/watch?v=jCehccEZzX4", "https://www.youtube.com/watch?v=aBKEt3MhNMM");

    private static   SpotifyApiParser apiParser = new SpotifyApiParser();


    private static YtTokenEntity token = new YtTokenEntity();
    @MockBean
    private YtTokenRepository repository;
    @Autowired
    private YtTokenService service;

    @BeforeAll
    static void dbSetUp(){
        tracklist.put("Killing Yourself To Live", List.of("BLACK SABBATH"));
        tracklist.put("Starman", List.of("David Bowie"));
        token.setToken("AIzaSyCwF33ycKkw7uUvLzKtkHAfrxR4XyvnNII");
        token.setQuota(true);
        apiParser.setSongList(tracklist);
    }
    @Test
    void multiThreadParse() throws InterruptedException {
        YoutubeApiParser youtubeApiParser = new YoutubeApiParser(apiParser, service);
        Mockito.when(service.getTokens()).thenReturn(List.of(token));
        youtubeApiParser.multiThreadParse();
        Assert.assertEquals(youtubeApiParser.getYoutubeUrls(), urlList);
    }
    @Test
    void testWithBrokenUrls() throws InterruptedException{
        YoutubeApiParser youtubeApiParser = new YoutubeApiParser(apiParser, service);
        Assert.assertThrows(NullPointerException.class, ()-> youtubeApiParser.multiThreadParse());
    }

}
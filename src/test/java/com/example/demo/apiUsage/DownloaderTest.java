package com.example.demo.apiUsage;

import com.example.demo.apiUsage.spotify.SpotifyApiParser;
import com.example.demo.apiUsage.youtube.YoutubeApiParser;
import com.example.demo.entity.YtTokenEntity;
import com.example.demo.service.YtTokenService;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.UUID;

class DownloaderTest {

   // @Test
   // void download() throws Exception {
   //     String token = UUID.randomUUID().toString();
   //     String url = "https://www.youtube.com/playlist?list=PLOTK54q5K4INNXaHKtmXYr6J7CajWjqeJ";
   //     Downloader downloader = new Downloader(new YoutubeApiParser(SpotifyApiParser.build(), new YtTokenService() {
   //         @Override
   //         public List<YtTokenEntity> getTokens() {
   //             return null;
   //         }
//
   //         @Override
   //         public void save(YtTokenEntity token) {
//
   //         }
//
   //         @Override
   //         public void updateAllTokens() {
//
   //         }
   //     }), request);
   //     downloader.downloadFromYoutubePlaylist(token, url);
   // }

}
package com.example.demo.controller;

import com.example.demo.apiUsage.Downloader;
import com.example.demo.apiUsage.spotify.SpotifyApiParser;
import com.example.demo.apiUsage.youtube.YoutubeApiParser;
import com.example.demo.entity.AudioEntity;
import com.example.demo.entity.RoomEntity;
import com.example.demo.entity.StreamingDataEntity;
import com.example.demo.entity.YtTokenEntity;
import com.example.demo.service.AudioService;
import com.example.demo.service.RoomEntityService;
import com.example.demo.service.StreamingDataService;
import com.example.demo.service.YtTokenService;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.print.attribute.standard.Media;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@RestController
@RequestMapping("/api/audio")
public class AudioRESTController {
    private final AudioService audioService;
    private final YoutubeApiParser youtubeApiParser;
    private final SpotifyApiParser spotifyApiParser;
    private final Downloader downloader;
    private final RoomEntityService roomEntityService;
    private final StreamingDataService streamingDataService;
    Logger logger = LoggerFactory.getLogger(AudioRESTController.class);

    public AudioRESTController(AudioService audioService, YoutubeApiParser apiParser, SpotifyApiParser spotifyApiParser, Downloader downloader, RoomEntityService roomEntityService, StreamingDataService streamingDataService) {
        this.audioService = audioService;
        this.youtubeApiParser = apiParser;
        this.spotifyApiParser = spotifyApiParser;
        this.downloader = downloader;
        this.roomEntityService = roomEntityService;
        this.streamingDataService = streamingDataService;
    }

    @GetMapping("/getResource/{name}")
    public ResponseEntity<Resource> getAudioByName(@PathVariable("name") String name) throws IOException {
        return ResponseEntity.ok(new ByteArrayResource(FileUtils.readFileToByteArray(new File(audioService.getAudio(name).getPath()))));
    }


   @PostMapping("/delete")
   public ResponseEntity<String> deleteTrack(@RequestParam(value = "track_name")String trackName, @RequestParam(value = "room_url")String roomUrl){
        logger.info(trackName + " " + roomUrl);
        StringBuilder correctRoomUrl = new StringBuilder(roomUrl);
        if (roomUrl.contains("?")){
            correctRoomUrl.delete(roomUrl.indexOf("?"), roomUrl.length());
        }
        RoomEntity room = roomEntityService.loginToRoom(correctRoomUrl.toString());
       AudioEntity entity = audioService.getAudio(trackName);
       for (int i = 0; i < room.getAudioEntity().size(); i++){
           if (room.getAudioEntity().get(i).equals(entity)){
               room.getAudioEntity().remove(i);
           }
       }
       roomEntityService.updateRoom(room);
       return ResponseEntity.ok("deleted");
   }
   @GetMapping("/all")
   public ResponseEntity<List<String>> getAllAudiosName(){
       return ResponseEntity.ok(audioService.getAllAudio());
   }
   @GetMapping("/getRoomAudio/{room_url}")
    public ResponseEntity<List<String>> getAllAudioFromRoom(@PathVariable String room_url){
        return ResponseEntity.ok(roomEntityService.getAudioFromRoom(room_url));
   }
    @GetMapping("/getStreamingData/{url}")
    public ResponseEntity<StreamingDataEntity> getData(@PathVariable String url){
        return ResponseEntity.ok(streamingDataService.get(url));
    }

    @PostMapping(value = "/postStreamingData", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> saveData(@RequestBody StreamingDataEntity streamingDataEntity){
        StringBuffer room = new StringBuffer(streamingDataEntity.getRoom());
        if (streamingDataEntity.getRoom().contains("?")) {
            room.delete(streamingDataEntity.getRoom().indexOf("?"), streamingDataEntity.getRoom().length());
            streamingDataEntity.setRoom(room.toString());
        }
        if(streamingDataService.get(room.toString()) == null){
            streamingDataEntity.setCondition("false");
            streamingDataService.save(streamingDataEntity);
            return ResponseEntity.ok("save");
        }
        StreamingDataEntity dataEntity = streamingDataService.get(room.toString());
        dataEntity.setRoom(room.toString());
        dataEntity.setTime(streamingDataEntity.getTime());
        dataEntity.setCondition(streamingDataEntity.getCondition());
        dataEntity.setTrack(streamingDataEntity.getTrack());
        streamingDataService.save(dataEntity);
        return ResponseEntity.ok("save");
    }

}

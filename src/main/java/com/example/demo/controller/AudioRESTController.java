package com.example.demo.controller;

import com.example.demo.apiUsage.Downloader;
import com.example.demo.apiUsage.spotify.SpotifyApiParser;
import com.example.demo.apiUsage.youtube.YoutubeApiParser;
import com.example.demo.entity.AudioEntity;
import com.example.demo.entity.RoomEntity;
import com.example.demo.entity.StreamingDataEntity;

import com.example.demo.service.AudioService;
import com.example.demo.service.RoomEntityService;
import com.example.demo.service.StreamingDataService;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.*;
import java.util.List;

//This is a rest controller for working with audio.
// The client sends certain requests, receives the necessary data in the response,
// or vice versa adds something to the server.

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

    //Gets a stream of bytes from a file. The client receives it and sends it to the audio player.
    @GetMapping("/getResource/{name}")
    public ResponseEntity<Resource> getAudioByName(@PathVariable("name") String name) throws IOException {
        return ResponseEntity.ok(new ByteArrayResource(FileUtils.readFileToByteArray(new File(audioService.getAudio(name).getPath()))));
    }

    //Remove track from room audio
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
   //Get all audio from audioEntity DB
   @GetMapping("/all")
   public ResponseEntity<List<String>> getAllAudiosName(){
       return ResponseEntity.ok(audioService.getAllAudio());
   }
    //Get all audio from room audio
   @GetMapping("/getRoomAudio/{room_url}")
    public ResponseEntity<List<String>> getAllAudioFromRoom(@PathVariable String room_url){
        return ResponseEntity.ok(roomEntityService.getAudioFromRoom(room_url));
   }

   //Stupid simulation of peer to peer connection. Room has 2 type of user - streamer and listener.
   // Every a few second listener get data from DB which contains streamer's audio player status
    @GetMapping("/getStreamingData/{url}")
    public ResponseEntity<StreamingDataEntity> getData(@PathVariable String url){
        return ResponseEntity.ok(streamingDataService.get(url));
    }
    //Stupid simulation of peer to peer connection. Room has 2 type of user - streamer and listener.
    // Every a few second streamer post data to DB which contains streamer's audio player status
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

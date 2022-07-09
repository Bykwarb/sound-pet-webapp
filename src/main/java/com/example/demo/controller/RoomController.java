package com.example.demo.controller;

import com.example.demo.apiUsage.Downloader;
import com.example.demo.apiUsage.spotify.SpotifyApiParser;
import com.example.demo.apiUsage.youtube.YoutubeApiParser;
import com.example.demo.entity.AudioEntity;
import com.example.demo.entity.RoomEntity;
import com.example.demo.entity.YtTokenEntity;
import com.example.demo.service.AudioService;
import com.example.demo.service.RoomEntityService;
import com.example.demo.service.UserEntityService;
import com.example.demo.service.YtTokenService;
import com.example.demo.utils.aop.Monitor;
import org.apache.http.client.HttpResponseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

@Controller()
@PropertySource("classpath:directory.properties")
public class RoomController {
    private static final Logger logger = LoggerFactory.getLogger(LoginController.class);
    @Value("${room-controller.absolutePathToMusicStorage}")
    private String absolutePathToMusicStorage;

    private final RoomEntityService roomEntityService;
    private final UserEntityService userEntityService;
    private final PasswordEncoder passwordEncoder;
    private final YoutubeApiParser youtubeApiParser;
    private final SpotifyApiParser spotifyApiParser;
    private final AudioService audioService;
    private final Downloader downloader;
    private final YtTokenService service;
    public RoomController(RoomEntityService roomEntityService, UserEntityService userEntityService, PasswordEncoder passwordEncoder, YoutubeApiParser youtubeApiParser, SpotifyApiParser spotifyApiParser, AudioService audioService, Downloader downloader, YtTokenService service) {
        this.roomEntityService = roomEntityService;
        this.userEntityService = userEntityService;
        this.passwordEncoder = passwordEncoder;
        this.youtubeApiParser = youtubeApiParser;
        this.spotifyApiParser = spotifyApiParser;
        this.audioService = audioService;
        this.downloader = downloader;
        this.service = service;
    }
    @Monitor
    @RequestMapping("/room")
    public String showRoom(){
        return "/main/Room_Create";
    }

    @PostMapping("/room/create")
    public ModelAndView createRoom(HttpServletRequest request, @RequestParam("name") String name, @RequestParam("password") String password){
        logger.info("User in createRoom controller");
        if (request.getSession().getAttribute("username") == null){
            return new ModelAndView("redirect:/login");
        }
        RoomEntity entity = new RoomEntity();
        entity.setCreator(userEntityService.loginUser(request.getSession().getAttribute("email").toString()));
        entity.setUrl(UUID.randomUUID().toString());
        entity.setName(name);
        entity.setPassword(passwordEncoder.encode(password));
        roomEntityService.createNewRoom(entity);
        request.getSession().setAttribute("room_url", entity.getUrl());
        return new ModelAndView("redirect:/room/download/" + entity.getUrl());
    }

   @PostMapping ("/room/login")
   public ModelAndView enterTheRoom(HttpServletRequest request, @RequestParam("room_url") String room_url, @RequestParam("password") String password){
       if (request.getSession().getAttribute("username") == null){
           return new ModelAndView("redirect:/login");
       }
       RoomEntity room = roomEntityService.loginToRoom(room_url);
       ModelAndView modelAndView;
       if (room == null) {
           modelAndView = new ModelAndView("redirect:/room", "message", new String("Room not found!"));
           logger.info("Room not found!");
           return modelAndView;
       } else if (!passwordEncoder.matches(password, room.getPassword())) {
           modelAndView = new ModelAndView("redirect:/room", "message", new String("Error password!"));
           logger.info("Error password!");
           return modelAndView;
       }else if(room.getAudioEntity().isEmpty()){
           request.getSession().setAttribute("room_url", room.getUrl());
           return new ModelAndView("redirect:/room/download/" + room_url);
       }
       request.getSession().setAttribute("room_url", room.getUrl());
       return new ModelAndView("redirect:/room/content/" + room_url);
   }

    @GetMapping("/room/download/{url}")
    public String showUploadContent(@PathVariable("url") String url, HttpServletRequest request){
        logger.info("User in showUploadContent controller");
        if (request.getSession().getAttribute("username") == null){
            return "redirect:/login";
        }
        return "/main/Upload_Content";
    }

    @GetMapping("/room/content/{url}")
    public String showRoomContent(@PathVariable("url") String url, HttpServletRequest request){
        logger.info("User in showRoomContent controller");
        RoomEntity room = roomEntityService.loginToRoom(url);
        if (request.getSession().getAttribute("username") == null){
            return "redirect:/login";
        }
        if(request.getSession().getAttribute("email").toString().compareTo(room.getCreator().getEmail()) != 0){
            return "/main/listener";
        }
        return "/main/streamer";
    }

    @PostMapping("/download")
    public String download(HttpServletRequest request, @RequestParam("url") String url) throws Exception {
        if (request.getSession().getAttribute("username") == null){
            return "redirect:/login";
        }
        List<String> albumList = audioService.getAllAudio();
        List<String> keychain = new ArrayList<>();
        logger.info(albumList.toString());
        RoomEntity room = roomEntityService.loginToRoom(request.getSession().getAttribute("room_url").toString());
        if (url.contains("https://open.spotify.com/playlist/")){
            spotifyApiParser.setPlaylistUrl(new StringBuffer(url));
            try {
                spotifyApiParser.parse();
            }catch (HttpResponseException exception){
                return "redirect:/room/download/" + room.getUrl();
            }
        }else {
            spotifyApiParser.setAlbumUrl(new StringBuffer(url));
            try {
                spotifyApiParser.parseAlbum();
            }catch (HttpResponseException exception){
                return "redirect:/room/download/" + room.getUrl();
            }

        }
        spotifyApiParser.getSongList().forEach((x,y) ->{
            keychain.add(x);
        });
        for (String s : keychain) {
            if (albumList.contains(s)) {
                spotifyApiParser.getSongList().remove(s);
            }
        }
        youtubeApiParser.multiThreadParse();
        String token = request.getSession().getAttribute("room_url").toString();
        downloader.downloadFromTxt(token);
        List<String> filePathList = downloader.extractFileNames(absolutePathToMusicStorage + token);
        List<AudioEntity> audioEntities = new ArrayList<>();
        AtomicInteger integer = new AtomicInteger(0);
        spotifyApiParser.getSongList().forEach((x,y)->{
            if (!albumList.contains(x)){
                try {
                    audioService.saveAudio(x, filePathList.get(integer.get()));
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
            integer.getAndIncrement();
        });
        for (int i = 0; i < keychain.size(); i++){
            audioEntities.add(audioService.getAudio(keychain.get(i)));
        }
        room.setAudioEntity(audioEntities);
        roomEntityService.updateRoom(room);
        return "redirect:/room/content/"+ room.getUrl();
    }
    @PostMapping("/room/add")
    public String addMusicToRoom(HttpServletRequest request,@RequestParam("resource_url") String url) throws Exception {
        if (request.getSession().getAttribute("username") == null){
            return "redirect:/login";
        }
        String room_url = request.getSession().getAttribute("room_url").toString();
        List<AudioEntity> audioEntities = new ArrayList<>();
        logger.info(String.valueOf(url.contains("https://api.spotify.com/v1/playlists/")));
        logger.info(String.valueOf(url.contains("https://api.spotify.com/v1/albums/")));
        List<String> albumList = audioService.getAllAudio();
        List<String> keychain = new ArrayList<>();
        logger.info(albumList.toString());
        if (url.contains("https://open.spotify.com/playlist/")){
            spotifyApiParser.setPlaylistUrl(new StringBuffer(url));
            try {
                spotifyApiParser.parse();
            }catch (HttpResponseException exception){
                return "redirect:/room/content/" + room_url;
            }
            update(room_url, audioEntities, albumList, keychain);
        }
        else if(url.contains("https://open.spotify.com/album/")){
            spotifyApiParser.setAlbumUrl(new StringBuffer(url));
            try {
                spotifyApiParser.parseAlbum();
            }catch (HttpResponseException exception){
                return "redirect:/room/content/" + room_url;
            }
            update(room_url, audioEntities, albumList, keychain);
        }
        else {
            downloader.updateFromYoutube(room_url, url);
            List<File> files = downloader.extractFiles(absolutePathToMusicStorage + room_url + "/update_" + downloader.getUpdateTime());
            AtomicInteger counter = new AtomicInteger();
            files.forEach(e->{
                String name = files.get(counter.get()).getName();
                String path = files.get(counter.get()).getAbsolutePath();
                try {
                    if(audioService.getAudio(name) == null){
                        audioService.saveAudio(name, path);
                    }
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
                audioEntities.add(audioService.getAudio(name));
                counter.getAndIncrement();
            });
        }

        RoomEntity room = roomEntityService.loginToRoom(room_url);
        audioEntities.forEach(e->{
            if(!room.getAudioEntity().contains(e)){
                room.getAudioEntity().add(e);
            }
        });
        roomEntityService.updateRoom(room);
        return "redirect:/room/content/"+ room.getUrl();
    }

    private void update(String room_url, List<AudioEntity> audioEntities, List<String> albumList, List<String> keychain) throws Exception {
        spotifyApiParser.getSongList().forEach((x,y) ->{
            keychain.add(x);
        });
        logger.info(keychain.toString());
        for (String s : keychain) {
            if (albumList.contains(s)) {
                spotifyApiParser.getSongList().remove(s);
            }
        }
        logger.info(spotifyApiParser.getSongList().toString());
        youtubeApiParser.multiThreadParse();
        downloader.updateFromSpotify(room_url);
        List<String> filePathList = downloader.extractFileNames(absolutePathToMusicStorage + room_url + "/update_" + downloader.getUpdateTime());
        logger.info(filePathList.toString());
        AtomicInteger integer = new AtomicInteger(0);
        spotifyApiParser.getSongList().forEach((x,y)->{
            if (!albumList.contains(x)){
                try {
                    audioService.saveAudio(x, filePathList.get(integer.get()));
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
            integer.getAndIncrement();
        });
        for (int i = 0; i < keychain.size(); i++){
            audioEntities.add(audioService.getAudio(keychain.get(i)));
        }
    }

    @PostMapping("/downloadFromYtPlaylist")
    public String ytPlaylistDownload(HttpServletRequest request, @RequestParam("url") String url) throws Exception{
        if (request.getSession().getAttribute("username") == null){
            return "redirect:/login";
        }
        RoomEntity room = roomEntityService.loginToRoom(request.getSession().getAttribute("room_url").toString());
        String token = request.getSession().getAttribute("room_url").toString();
        downloader.downloadFromYoutubePlaylist(token, url);
        List<File> files = downloader.extractFiles(absolutePathToMusicStorage + token);
        List<AudioEntity> audioEntities = new ArrayList<>();
        AtomicInteger counter = new AtomicInteger();
        files.forEach(e->{
            String name = files.get(counter.get()).getName();
            String path = files.get(counter.get()).getAbsolutePath();
            try {
                if(audioService.getAudio(name) == null){
                    audioService.saveAudio(name, path);
                }
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
            audioEntities.add(audioService.getAudio(name));
            counter.getAndIncrement();
        });
        room.setAudioEntity(audioEntities);
        roomEntityService.updateRoom(room);
        return "redirect:/room/content/"+ room.getUrl();
    }
}

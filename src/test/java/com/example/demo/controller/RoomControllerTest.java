package com.example.demo.controller;

import com.example.demo.entity.RoomEntity;
import com.example.demo.repository.AudioRepository;
import com.example.demo.repository.RoomEntityRepository;
import com.example.demo.service.AudioService;
import com.example.demo.service.RoomEntityService;
import com.example.demo.service.UserEntityService;
import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.*;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource("classpath:directory.properties")
@AutoConfigureMockMvc
class RoomControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private RoomEntityRepository repository;
    @Autowired
    private RoomEntityService service;
    @Autowired
    private UserEntityService userEntityService;
    @Autowired
    private AudioRepository audioRepository;
    @Autowired
    private AudioService audioService;
    @Value("${downloader.absolutePathToMusicStorage}")
    private String absolutePathToMusicStorage;

    @Test
    void createRoom() throws Exception {
        RequestBuilder builder = MockMvcRequestBuilders
                .post("/room/create")
                .sessionAttr("username", "Bykwarb")
                .sessionAttr("email", "test@gmail.com")
                .param("name", "testRoom")
                .param("password", "testPass");
        MvcResult result = mockMvc.perform(builder).andReturn();
        assertEquals(302, result.getResponse().getStatus());
        assertNotNull(repository.findByName("testRoom"));
        repository.delete(repository.findByName("testRoom"));
    }
    @Test
    void download() throws Exception {
        File file = new File(absolutePathToMusicStorage + "testRoom");
        if (file.exists()){
            FileUtils.cleanDirectory(new File(absolutePathToMusicStorage + "testRoom"));
        }
        RoomEntity room = new RoomEntity();
        room.setName("testRoom");
        room.setUrl("testRoom");
        room.setCreator(userEntityService.loginUser("nebykwarb@gmail.com"));
        room.setPassword("111111");
        service.createNewRoom(room);

        RequestBuilder builder = MockMvcRequestBuilders
                .post("/download")
                .sessionAttr("username", "Bykwarb")
                .sessionAttr("room_url", "testRoom")
                .param("url", "https://open.spotify.com/playlist/4DZFYu8xN1fOo4XGV1kFfI");
        MvcResult result = mockMvc.perform(builder).andReturn();

        List<String> trackName = List.of("By Myself", "A Place for My Head", "Forgotten");
        List<String> responseTracks = new ArrayList<>();
        room = service.loginToRoom("testRoom");
        room.getAudioEntity().forEach(e ->{
            responseTracks.add(e.getName());
        });
        room.getAudioEntity().clear();
        service.updateRoom(room);
        repository.delete(repository.findByName("testRoom"));
        if (file.exists()){
            FileUtils.cleanDirectory(new File(absolutePathToMusicStorage + "testRoom"));
        }

        assertEquals(302, result.getResponse().getStatus());
        assertEquals(trackName, responseTracks);
    }

    @Test
    void ytPlaylistDownload() throws Exception {
        File file = new File(absolutePathToMusicStorage + "testRoom");
        if (file.exists()){
            FileUtils.cleanDirectory(new File(absolutePathToMusicStorage + "testRoom"));
        }
        RoomEntity room = new RoomEntity();
        room.setName("testRoom");
        room.setUrl("testRoom");
        room.setCreator(userEntityService.loginUser("nebykwarb@gmail.com"));
        room.setPassword("111111");
        service.createNewRoom(room);

        RequestBuilder builder = MockMvcRequestBuilders
                .post("/downloadFromYtPlaylist")
                .sessionAttr("username", "Bykwarb")
                .sessionAttr("room_url", "testRoom")
                .param("url", "https://www.youtube.com/watch?v=eDtO91aGSiY");
        MvcResult result = mockMvc.perform(builder).andReturn();

        List<String> trackName = List.of("Who Taught You How to Hate.webm");
        List<String> responseTracks = new ArrayList<>();
        room = service.loginToRoom("testRoom");
        room.getAudioEntity().forEach(e ->{
            responseTracks.add(e.getName());
        });
        room.getAudioEntity().clear();
        service.updateRoom(room);
        audioRepository.delete(audioService.getAudio("Who Taught You How to Hate.webm"));
        repository.delete(repository.findByName("testRoom"));
        if (file.exists()){
            FileUtils.cleanDirectory(new File(absolutePathToMusicStorage + "testRoom"));
        }

        assertEquals(302, result.getResponse().getStatus());
        assertEquals(trackName, responseTracks);

    }

    @Test
    void addMusicToRoom() throws Exception {
        File file = new File(absolutePathToMusicStorage + "testRoom");
        if (file.exists()){
            FileUtils.cleanDirectory(new File(absolutePathToMusicStorage + "testRoom"));
        }
        RoomEntity room = new RoomEntity();
        room.setName("testRoom");
        room.setUrl("testRoom");
        room.setCreator(userEntityService.loginUser("nebykwarb@gmail.com"));
        room.setPassword("111111");
        service.createNewRoom(room);
        RequestBuilder builder = MockMvcRequestBuilders
                .post("/room/add")
                .sessionAttr("username", "Bykwarb")
                .sessionAttr("room_url", "testRoom")
                .param("resource_url", "https://www.youtube.com/watch?v=eDtO91aGSiY");
        MvcResult result = mockMvc.perform(builder).andReturn();

        List<String> trackName = List.of("Who Taught You How to Hate.webm");
        List<String> responseTracks = new ArrayList<>();
        room = service.loginToRoom("testRoom");
        room.getAudioEntity().forEach(e ->{
            responseTracks.add(e.getName());
        });
        room.getAudioEntity().clear();
        service.updateRoom(room);
        audioRepository.delete(audioService.getAudio("Who Taught You How to Hate.webm"));
        repository.delete(repository.findByName("testRoom"));
        if (file.exists()){
            FileUtils.cleanDirectory(new File(absolutePathToMusicStorage + "testRoom"));
        }

        assertEquals(302, result.getResponse().getStatus());
        assertEquals(trackName, responseTracks);



    }
}
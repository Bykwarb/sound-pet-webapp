package com.example.demo.controller;



import com.example.demo.entity.RoomEntity;
import com.example.demo.entity.StreamingDataEntity;
import com.example.demo.repository.RoomEntityRepository;
import com.example.demo.service.*;
import com.google.gson.Gson;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
class AudioRESTControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private AudioService audioService;

    @Autowired
    private RoomEntityService service;

    @Autowired
    private RoomEntityRepository repository;

    @Test
    void getAudioByName() throws Exception {
        RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/api/audio/getResource/Junkie");
        MvcResult result = mockMvc.perform(requestBuilder).andReturn();
        assertNotNull(result.getResponse());
    }

    @Test
    void deleteTrack() throws Exception {
        String name = UUID.randomUUID().toString();
        audioService.saveAudio(name, "///");
        RoomEntity room = new RoomEntity();
        room.setUrl(name);
        room.setAudioEntity(List.of(audioService.getAudio(name)));
        service.createNewRoom(room);
        RequestBuilder requestBuilder = MockMvcRequestBuilders.post("/api/audio/delete?track_name=" + name + "&room_url=" + name);
        MvcResult result = mockMvc.perform(requestBuilder).andReturn();
        assertEquals(200, result.getResponse().getStatus());
        repository.delete(room);
    }

    @Test
    void getAllAudiosName() throws Exception {
        RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/api/audio/all");
        MvcResult result = mockMvc.perform(requestBuilder).andReturn();
        assertEquals(200, result.getResponse().getStatus());
    }

    @Test
    void getAllAudioFromRoom() throws Exception {
        RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/api/audio/getRoomAudio/71d32b5d-28f8-4c62-bae1-d06a941eba13");
        MvcResult result = mockMvc.perform(requestBuilder).andReturn();
        assertEquals(200, result.getResponse().getStatus());
    }
    @Test
    void getData() throws Exception {
        RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/api/audio/getStreamingData/71d32b5d-28f8-4c62-bae1-d06a941eba13");
        MvcResult result = mockMvc.perform(requestBuilder).andReturn();
        assertEquals(200, result.getResponse().getStatus());
    }

    @Test
    void saveData() throws Exception {
        StreamingDataEntity entity = new StreamingDataEntity();
        entity.setTrack("1");
        entity.setCondition("false");
        entity.setRoom("1");
        entity.setTime("1");
        String json = new Gson().toJson(entity);
        RequestBuilder requestBuilder = MockMvcRequestBuilders.post("/api/audio/postStreamingData").contentType(MediaType.APPLICATION_JSON).content(json);
        MvcResult result = mockMvc.perform(requestBuilder).andReturn();
        assertEquals(200, result.getResponse().getStatus());
    }
}
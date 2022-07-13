package com.example.demo.service;

import com.example.demo.entity.AudioEntity;
import com.example.demo.entity.RoomEntity;
import com.example.demo.repository.RoomEntityRepository;
import org.junit.Assert;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
@ExtendWith(MockitoExtension.class)
class RoomEntityServiceImplTest {
    @Mock
    private RoomEntityRepository repository;
    @InjectMocks
    private RoomEntityServiceImpl service;

    private static RoomEntity room = new RoomEntity();
    @BeforeEach
    void setRoom(){
        room.setId(1l);
        room.setName("1221");
        room.setUrl("1111");
        room.setPassword("123456");
        room.setAudioEntity(List.of(new AudioEntity(), new AudioEntity(), new AudioEntity()));
    }

    @Test
    void createNewRoom() {
        service.createNewRoom(room);
        Mockito.verify(repository, Mockito.times(1)).save(room);
    }

    @Test
    void loginToRoom() {
        Mockito.when(repository.findByUrl("1111")).thenReturn(room);
        Assert.assertEquals(room, service.loginToRoom(room.getUrl()));
    }

    @Test
    void getAudioFromRoom() {
        Mockito.when(repository.findByUrl("1111")).thenReturn(room);
        Assert.assertEquals(room.getAudioEntity(), service.loginToRoom(room.getUrl()).getAudioEntity());
    }

    @Test
    void updateRoom() {
        service.updateRoom(room);
        Mockito.verify(repository, Mockito.times(1)).save(room);
    }
}
package com.example.demo.service;

import com.example.demo.entity.RoomEntity;
import com.example.demo.entity.UserEntity;

import java.util.List;

public interface RoomEntityService {
    void createNewRoom(RoomEntity room);
    RoomEntity loginToRoom(String generated_url);


    List<String> getAudioFromRoom(String room_url);

    void updateRoom(RoomEntity room);
}

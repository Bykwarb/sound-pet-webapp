package com.example.demo.service;

import com.example.demo.entity.StreamingDataEntity;

public interface StreamingDataService {
    StreamingDataEntity get(String room_url);
    void save(StreamingDataEntity entity);
}

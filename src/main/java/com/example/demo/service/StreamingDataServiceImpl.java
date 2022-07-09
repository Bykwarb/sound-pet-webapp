package com.example.demo.service;

import com.example.demo.entity.StreamingDataEntity;
import com.example.demo.repository.StreamingDataRepo;
import org.springframework.stereotype.Service;

@Service
public class StreamingDataServiceImpl implements StreamingDataService{
    private final StreamingDataRepo repository;

    public StreamingDataServiceImpl(StreamingDataRepo repository) {
        this.repository = repository;
    }

    @Override
    public StreamingDataEntity get(String room_url) {
        StreamingDataEntity entity = repository.findByRoom(room_url);
        return entity;
    }

    @Override
    public void save(StreamingDataEntity entity) {
        repository.save(entity);
    }
}

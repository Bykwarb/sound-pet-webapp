package com.example.demo.service;

import com.example.demo.entity.AudioEntity;
import com.example.demo.repository.AudioRepository;

import org.springframework.stereotype.Service;


import java.io.IOException;
import java.util.List;
@Service
public class AudioServiceImpl implements AudioService{
    private final AudioRepository audioRepository;
    public AudioServiceImpl(AudioRepository audioRepository) {
        this.audioRepository = audioRepository;

    }

    @Override
    public AudioEntity getAudio(String name) {
        AudioEntity entity = audioRepository.findByName(name);
        return entity;
    }

    @Override
    public void saveAudio(String name, String path) throws IOException {
        AudioEntity entity = new AudioEntity();
        entity.setName(name);
        entity.setPath(path);
        audioRepository.save(entity);
    }


    @Override
    public List<String> getAllAudio() {
        List<String> entities = audioRepository.getAllEntryNames();
        return entities;
    }


}

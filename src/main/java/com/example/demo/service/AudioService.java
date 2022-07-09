package com.example.demo.service;

import com.example.demo.entity.AudioEntity;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.List;

public interface AudioService {
    AudioEntity getAudio(String name);
    void saveAudio(String name, String url) throws IOException;


    List<String> getAllAudio();

}

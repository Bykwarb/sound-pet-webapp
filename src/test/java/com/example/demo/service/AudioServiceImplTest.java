package com.example.demo.service;

import com.example.demo.entity.AudioEntity;
import com.example.demo.repository.AudioRepository;
import org.junit.Assert;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
@ExtendWith(MockitoExtension.class)
class AudioServiceImplTest {

    @Mock
    private AudioRepository repository;

    @InjectMocks
    private AudioServiceImpl audioService;

    static AudioEntity audio = new AudioEntity();

    List<AudioEntity> entities = List.of(new AudioEntity(), new AudioEntity(), new AudioEntity());

    @BeforeEach
    public void setAudio(){
        audio.setName("Believer");
        audio.setArtistName("Ozzy");
        audio.setPath("///");
        audio.setId(1l);
    }
    @Test
    void getAudio() {
        Mockito.when(repository.findByName(audio.getName())).thenReturn(audio);
        Assert.assertEquals(audio, audioService.getAudio(audio.getName()));
    }

}
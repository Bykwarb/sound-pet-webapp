package com.example.demo.service;

import com.example.demo.entity.StreamingDataEntity;
import com.example.demo.repository.StreamingDataRepo;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
@ExtendWith(MockitoExtension.class)
class StreamingDataServiceImplTest {
    @Mock
    private StreamingDataRepo repo;

    @InjectMocks
    private StreamingDataServiceImpl service;

    static StreamingDataEntity entity = new StreamingDataEntity();

    {
        entity.setRoom("1111");
        entity.setTime("2222");
        entity.setCondition("false");
        entity.setTrack("Ozzy");
    }

    @Test
    void get() {
        Mockito.when(repo.findByRoom("1111")).thenReturn(entity);
        Assert.assertEquals(entity, service.get("1111"));
    }

    @Test
    void save() {
        service.save(entity);
        Mockito.verify(repo, Mockito.times(1)).save(entity);
    }
}
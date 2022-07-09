package com.example.demo.repository;

import com.example.demo.entity.AudioEntity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface AudioRepository extends CrudRepository<AudioEntity, Long> {
    AudioEntity findByName(String name);
    @Query(nativeQuery = true, value="SELECT name FROM audio_entity")
    List<String> getAllEntryNames();
}

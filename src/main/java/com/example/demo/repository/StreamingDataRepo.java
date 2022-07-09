package com.example.demo.repository;

import com.example.demo.entity.StreamingDataEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StreamingDataRepo extends CrudRepository<StreamingDataEntity, Long> {
    StreamingDataEntity findByRoom(String roomUrl);
}

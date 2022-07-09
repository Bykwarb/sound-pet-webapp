package com.example.demo.repository;

import com.example.demo.entity.RoomEntity;
import com.example.demo.entity.UserEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoomEntityRepository extends CrudRepository<RoomEntity, Long> {
    RoomEntity getByCreator(UserEntity user);
    RoomEntity findByUrl(String url);
}

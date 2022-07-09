package com.example.demo.repository;

import com.example.demo.entity.UserEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface UserRepository extends CrudRepository<UserEntity, Long> {
    UserEntity getByEmail(String email);
    boolean existsUserByEmail(String email);
}

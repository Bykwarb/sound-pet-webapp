package com.example.demo.repository;

import com.example.demo.entity.YtTokenEntity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface YtTokenRepository extends CrudRepository<YtTokenEntity, Long> {
    List<YtTokenEntity> findAllByQuota(boolean quota);
}

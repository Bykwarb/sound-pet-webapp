package com.example.demo.service;

import com.example.demo.entity.YtTokenEntity;

import java.util.List;

public interface YtTokenService {
    List<YtTokenEntity> getTokens();

    void save(YtTokenEntity token);

    void updateAllTokens();
}

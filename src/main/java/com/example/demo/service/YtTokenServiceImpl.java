package com.example.demo.service;

import com.example.demo.entity.YtTokenEntity;
import com.example.demo.repository.YtTokenRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class YtTokenServiceImpl implements YtTokenService{
    private final YtTokenRepository repository;

    public YtTokenServiceImpl(YtTokenRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<YtTokenEntity> getTokens() {
        return repository.findAllByQuota(true);
    }

    @Override
    public void save(YtTokenEntity token) {
        repository.save(token);
    }

    @Scheduled(fixedDelay = 86400000)
    @Override
    public void updateAllTokens() {
        List<YtTokenEntity> tokens = repository.findAllByQuota(false);
        tokens.forEach(e->{
            e.setQuota(true);
            repository.save(e);
        });
    }

}

package com.example.demo.service;

import com.example.demo.repository.VerificationTokenEntityRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

@Service
@Transactional
public class VerificationTokenServiceImpl implements VerificationTokenEntityService{
    private final VerificationTokenEntityRepository repository;

    public VerificationTokenServiceImpl(VerificationTokenEntityRepository repository) {
        this.repository = repository;
    }

    @Scheduled(fixedDelay = 300000)
    @Override
    public void checkTokenVerification() {
        Date date = new Date();
        repository.deleteAllExpiredSince(date);
    }

}

package com.example.demo.service;

import com.example.demo.entity.UserEntity;
import com.example.demo.entity.VerificationTokenEntity;

public interface UserEntityService {
    UserEntity registerNewUserAccount(UserEntity userEntity);
    UserEntity loginUser(String email);
    void createVerificationToken(UserEntity userEntity, String token);
    VerificationTokenEntity getVerificationToken(String verificationToken);
    UserEntity registerAfterConfirmation(UserEntity user);
    UserEntity updateData(UserEntity userEntity);
}

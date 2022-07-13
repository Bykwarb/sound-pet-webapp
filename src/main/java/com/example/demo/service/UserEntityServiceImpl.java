package com.example.demo.service;

import com.example.demo.entity.UserEntity;
import com.example.demo.entity.VerificationTokenEntity;
import com.example.demo.exceptions.UserAlreadyExistsException;
import com.example.demo.repository.UserRepository;
import com.example.demo.repository.VerificationTokenEntityRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserEntityServiceImpl implements UserEntityService{
    private final UserRepository userRepository;
    private final VerificationTokenEntityRepository tokenEntityRepository;
    private final PasswordEncoder passwordEncoder;

    public UserEntityServiceImpl(UserRepository userRepository, VerificationTokenEntityRepository tokenEntityRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.tokenEntityRepository = tokenEntityRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public UserEntity registerNewUserAccount(UserEntity userEntity) {
        if(emailExist(userEntity.getEmail())){
            throw new UserAlreadyExistsException("There is an account with that email address: " + userEntity.getEmail());
        }
        userEntity.setPassword(passwordEncoder.encode(userEntity.getPassword()));
        return userRepository.save(userEntity);
    }

    @Override
    public UserEntity loginUser(String email) {
        UserEntity user = userRepository.getByEmail(email);
        return user;
    }

    @Override
    public void createVerificationToken(UserEntity userEntity, String token) {
        VerificationTokenEntity myToken = new VerificationTokenEntity(token, userEntity);
        tokenEntityRepository.save(myToken);
    }

    @Override
    public VerificationTokenEntity getVerificationToken(String verificationToken) {
        return tokenEntityRepository.findByToken(verificationToken);
    }

    @Override
    public UserEntity registerAfterConfirmation(UserEntity user) {
        return userRepository.save(user);
    }

    @Override
    public UserEntity updateData(UserEntity userEntity) {
        return null;
    }
    private boolean emailExist(String email){
        return userRepository.existsUserByEmail(email);
    }


}

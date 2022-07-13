package com.example.demo.service;

import com.example.demo.entity.UserEntity;
import com.example.demo.repository.UserRepository;
import com.example.demo.repository.VerificationTokenEntityRepository;
import org.aspectj.lang.annotation.Around;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class UserEntityServiceImplTest {
    @Mock
    private UserRepository repository;
    @Mock
    private VerificationTokenEntityRepository tokenEntityRepository;
    @InjectMocks
    private UserEntityServiceImpl service;
    @Mock
    private PasswordEncoder encoder;

    private static UserEntity entity = new UserEntity();
    {
        entity.setUsername("By");
        entity.setPassword("1221");
        entity.setRepassword("1221");
        entity.setEnabled(false);
        entity.setRegistrationDate(new Date());
        entity.setEmail("by@gmail.com");

    }

    @Test
    void registerNewUserAccount() {
        service.registerNewUserAccount(entity);
        Mockito.verify(repository, Mockito.times(1)).save(entity);
    }

    @Test
    void loginUser() {
        Mockito.when(repository.getByEmail("by@gmail.com")).thenReturn(entity);
        assertEquals(entity, service.loginUser("by@gmail.com"));
    }

}
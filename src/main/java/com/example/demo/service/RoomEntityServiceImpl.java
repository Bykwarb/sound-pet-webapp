package com.example.demo.service;

import com.example.demo.entity.RoomEntity;
import com.example.demo.repository.RoomEntityRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class RoomEntityServiceImpl implements RoomEntityService{
    private final RoomEntityRepository repository;
    private final PasswordEncoder passwordEncoder;

    public RoomEntityServiceImpl(RoomEntityRepository repository, PasswordEncoder passwordEncoder) {
        this.repository = repository;
        this.passwordEncoder = passwordEncoder;
    }


    @Override
    public void createNewRoom(RoomEntity room) {
        repository.save(room);
    }

    @Override
    public RoomEntity loginToRoom(String generated_url) {
        return repository.findByUrl(generated_url);
    }

    @Override
    public List<String> getAudioFromRoom(String room_url){
        List<String> response = new ArrayList<>();
        repository.findByUrl(room_url).getAudioEntity().forEach(e -> {
            response.add(e.getName());
        });
        return response;
    }

    @Override
    public void updateRoom(RoomEntity room) {
        repository.save(room);
    }
}

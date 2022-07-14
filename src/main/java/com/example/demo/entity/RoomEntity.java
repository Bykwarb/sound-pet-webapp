package com.example.demo.entity;

import javax.persistence.*;
import java.util.List;

@Entity
public class RoomEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String url;
    private String password;
    @OneToOne
    @JoinColumn(name = "creator_id")
    private UserEntity creator;
    @ManyToMany(targetEntity = AudioEntity.class, cascade = CascadeType.REMOVE, fetch = FetchType.EAGER)
    private List<AudioEntity> audioEntity;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public UserEntity getCreator() {
        return creator;
    }

    public void setCreator(UserEntity creator) {
        this.creator = creator;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public List<AudioEntity> getAudioEntity() {
        return audioEntity;
    }

    public void setAudioEntity(List<AudioEntity> audioEntity) {
        this.audioEntity = audioEntity;
    }


}

package com.example.demo.entity;

import javax.persistence.*;
import java.util.List;
import java.util.Objects;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RoomEntity room = (RoomEntity) o;
        return Objects.equals(id, room.id) && Objects.equals(name, room.name) && Objects.equals(url, room.url) && Objects.equals(password, room.password) && Objects.equals(creator, room.creator) && Objects.equals(audioEntity, room.audioEntity);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, url, password, creator, audioEntity);
    }

    @Override
    public String toString() {
        return "RoomEntity{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", url='" + url + '\'' +
                ", password='" + password + '\'' +
                ", creator=" + creator +
                ", audioEntity=" + audioEntity +
                '}';
    }
}

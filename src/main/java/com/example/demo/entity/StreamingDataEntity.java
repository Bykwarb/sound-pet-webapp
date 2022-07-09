package com.example.demo.entity;

import javax.persistence.*;

@Entity
public class StreamingDataEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    @Column(name = "room_url", unique = true)
    String room;
    @Column(name = "track_name")
    String track;
    @Column(name = "music_time")
    String time;
    @Column(name = "player_conditon")
    String condition;

    public StreamingDataEntity() {
    }

    public StreamingDataEntity(String room_url, String track_Name, String time, String condition) {
        this.room = room_url;
        this.track = track_Name;
        this.time = time;
        this.condition = condition;
    }

    public String getRoom() {
        return room;
    }

    public void setRoom(String room) {
        this.room = room;
    }

    public String getTrack() {
        return track;
    }

    public void setTrack(String track) {
        this.track = track;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getCondition() {
        return condition;
    }

    public void setCondition(String condition) {
        this.condition = condition;
    }
}


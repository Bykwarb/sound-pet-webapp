package com.example.demo.entity;

import javax.persistence.*;

@Entity
public class YtTokenEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    @Column(unique = true)
    private String token;
    @Column(columnDefinition = "boolean default true")
    private boolean quota;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public boolean isQuota() {
        return quota;
    }

    public void setQuota(boolean quota) {
        this.quota = quota;
    }
}

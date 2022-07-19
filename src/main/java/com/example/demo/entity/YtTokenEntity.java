package com.example.demo.entity;

import javax.persistence.*;
import java.util.Objects;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        YtTokenEntity token1 = (YtTokenEntity) o;
        return id == token1.id && quota == token1.quota && Objects.equals(token, token1.token);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, token, quota);
    }

    @Override
    public String toString() {
        return "YtTokenEntity{" +
                "id=" + id +
                ", token='" + token + '\'' +
                ", quota=" + quota +
                '}';
    }
}

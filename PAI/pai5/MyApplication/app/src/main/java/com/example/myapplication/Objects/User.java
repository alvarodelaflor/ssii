package com.example.myapplication.Objects;

import java.io.Serializable;

public class User implements Serializable {
    private String id;
    private String publicKey;

    public User() {
    }

    public User(String id, String publicKey) {
        this.id = id;
        this.publicKey = publicKey;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPublicKey() {
        return publicKey;
    }

    public void setPublicKey(String publicKey) {
        this.publicKey = publicKey;
    }

}

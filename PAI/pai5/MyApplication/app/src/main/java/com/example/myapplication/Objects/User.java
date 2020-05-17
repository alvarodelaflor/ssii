package com.example.myapplication.Objects;

import java.io.Serializable;

public class User implements Serializable {
    private String id;
    private String publicKey;
    private String privateKey;

    public User() {
    }

    public User(String id, String publicKey, String privateKey) {
        this.id = id;
        this.publicKey = publicKey;
        this.privateKey = privateKey;
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

    public String getPrivateKey() {
        return privateKey;
    }

    public void setPrivateKey(String privateKey) {
        this.privateKey = privateKey;
    }
}

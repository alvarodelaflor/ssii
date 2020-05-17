package com.example.myapplication.Objects;

import java.io.Serializable;
import java.util.Date;

public class Purchase implements Serializable {
    private String data;
    private String signature;
    public Purchase() {
    }

    public Purchase(String data, String signature) {
        this.data = data;
        this.signature = signature;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

}

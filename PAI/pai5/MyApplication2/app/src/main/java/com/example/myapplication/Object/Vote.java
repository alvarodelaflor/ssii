package com.example.myapplication.Object;

public class Vote {
    private String token;
    private String votation;

    public Vote() {
    }

    public Vote(String token, String votation) {
        this.token = token;
        this.votation = votation;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getVotation() {
        return votation;
    }

    public void setVotation(String votation) {
        this.votation = votation;
    }
}

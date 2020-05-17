package com.example.myapplication.Object;

public class UserAccess {

    private String userId;
    private String voteId;
    private String token;

    public UserAccess() {
    }

    public UserAccess(String userId, String voteId, String token) {
        this.userId = userId;
        this.voteId = voteId;
        this.token = token;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getVoteId() {
        return voteId;
    }

    public void setVoteId(String voteId) {
        this.voteId = voteId;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}

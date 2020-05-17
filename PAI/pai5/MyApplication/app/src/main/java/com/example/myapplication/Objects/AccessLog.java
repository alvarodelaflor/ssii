package com.example.myapplication.Objects;

import java.util.Date;

public class AccessLog {
    private Boolean status;
    private Date date;

    public AccessLog() {
    }

    public AccessLog(Boolean status, Date date) {
        this.status = status;
        this.date = date;
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}

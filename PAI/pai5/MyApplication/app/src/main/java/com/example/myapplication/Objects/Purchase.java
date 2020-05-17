package com.example.myapplication.Objects;

import java.io.Serializable;
import java.util.Date;

public class Purchase implements Serializable {
    private Integer bedNumber;
    private Integer tableNumber;
    private Integer chairNumber;
    private Integer armchairsNumber;
    private String userId;
    private Date buyDate;

    public Purchase() {
    }

    public Purchase(Integer bedNumber, Integer tableNumber, Integer chairNumber, Integer armchairsNumber, String userId, Date buyDate) {
        this.bedNumber = bedNumber;
        this.tableNumber = tableNumber;
        this.chairNumber = chairNumber;
        this.armchairsNumber = armchairsNumber;
        this.userId = userId;
        this.buyDate = buyDate;
    }

    public Integer getBedNumber() {
        return bedNumber;
    }

    public void setBedNumber(Integer bedNumber) {
        this.bedNumber = bedNumber;
    }

    public Integer getTableNumber() {
        return tableNumber;
    }

    public void setTableNumber(Integer tableNumber) {
        this.tableNumber = tableNumber;
    }

    public Integer getChairNumber() {
        return chairNumber;
    }

    public void setChairNumber(Integer chairNumber) {
        this.chairNumber = chairNumber;
    }

    public Integer getArmchairsNumber() {
        return armchairsNumber;
    }

    public void setArmchairsNumber(Integer armchairsNumber) {
        this.armchairsNumber = armchairsNumber;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Date getBuyDate() {
        return buyDate;
    }

    public void setBuyDate(Date buyDate) {
        this.buyDate = buyDate;
    }
}

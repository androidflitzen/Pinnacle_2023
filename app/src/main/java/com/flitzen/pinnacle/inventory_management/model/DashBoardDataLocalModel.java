package com.flitzen.pinnacle.inventory_management.model;

public class DashBoardDataLocalModel {
    private String remarks;
    private String userName;
    private String date;
    private String time;
    private String type;

    public DashBoardDataLocalModel(String remarks, String userName, String date, String time, String type) {
        this.remarks = remarks;
        this.userName = userName;
        this.date = date;
        this.time = time;
        this.type = type;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}

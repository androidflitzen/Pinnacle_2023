package com.flitzen.pinnacle.inventory_management.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class DashBoardDataModel {
    @SerializedName("total_order_pending")
    @Expose
    private String totalOrderPending;
    @SerializedName("total_order_deliver")
    @Expose
    private String totalOrderDeliver;
    @SerializedName("recent_activity")
    @Expose
    private List<RecentActivity> recentActivity = null;
    @SerializedName("status")
    @Expose
    private Integer status;
    @SerializedName("message")
    @Expose
    private String message;

    public String getTotalOrderPending() {
        return totalOrderPending;
    }

    public void setTotalOrderPending(String totalOrderPending) {
        this.totalOrderPending = totalOrderPending;
    }

    public String getTotalOrderDeliver() {
        return totalOrderDeliver;
    }

    public void setTotalOrderDeliver(String totalOrderDeliver) {
        this.totalOrderDeliver = totalOrderDeliver;
    }

    public List<RecentActivity> getRecentActivity() {
        return recentActivity;
    }

    public void setRecentActivity(List<RecentActivity> recentActivity) {
        this.recentActivity = recentActivity;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public class RecentActivity {

        @SerializedName("remarks")
        @Expose
        private String remarks;
        @SerializedName("user_name")
        @Expose
        private String userName;
        @SerializedName("date")
        @Expose
        private String date;
        @SerializedName("time")
        @Expose
        private String time;

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

    }
}

package com.flitzen.pinnacle.inventory_management.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class JobWorkStatusCount {
    @SerializedName("pennding_total")
    @Expose
    private String penndingTotal;
    @SerializedName("in_progress_total")
    @Expose
    private String inProgressTotal;
    @SerializedName("in_delay_total")
    @Expose
    private String inDelayTotal;
    @SerializedName("complete_total")
    @Expose
    private String completeTotal;
    @SerializedName("status")
    @Expose
    private Integer status;
    @SerializedName("message")
    @Expose
    private String message;

    public String getPenndingTotal() {
        return penndingTotal;
    }

    public void setPenndingTotal(String penndingTotal) {
        this.penndingTotal = penndingTotal;
    }

    public String getInProgressTotal() {
        return inProgressTotal;
    }

    public void setInProgressTotal(String inProgressTotal) {
        this.inProgressTotal = inProgressTotal;
    }

    public String getInDelayTotal() {
        return inDelayTotal;
    }

    public void setInDelayTotal(String inDelayTotal) {
        this.inDelayTotal = inDelayTotal;
    }

    public String getCompleteTotal() {
        return completeTotal;
    }

    public void setCompleteTotal(String completeTotal) {
        this.completeTotal = completeTotal;
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

}

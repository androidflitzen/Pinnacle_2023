package com.flitzen.pinnacle.inventory_management.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class CratesListInternalMovementsModel {
    @SerializedName("data")
    @Expose
    private List<Data> data = null;
    @SerializedName("status")
    @Expose
    private Integer status;
    @SerializedName("message")
    @Expose
    private String message;

    public List<Data> getData() {
        return data;
    }

    public void setData(List<Data> data) {
        this.data = data;
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

    public class Data {

        @SerializedName("crate_id")
        @Expose
        private String crateId;
        @SerializedName("crate_name")
        @Expose
        private String crateName;
        @SerializedName("total_items")
        @Expose
        private String totalItems;

        public String getCrateId() {
            return crateId;
        }

        public void setCrateId(String crateId) {
            this.crateId = crateId;
        }

        public String getCrateName() {
            return crateName;
        }

        public void setCrateName(String crateName) {
            this.crateName = crateName;
        }

        public String getTotalItems() {
            return totalItems;
        }

        public void setTotalItems(String totalItems) {
            this.totalItems = totalItems;
        }
    }
}

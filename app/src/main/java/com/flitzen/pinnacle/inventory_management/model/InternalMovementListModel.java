package com.flitzen.pinnacle.inventory_management.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class InternalMovementListModel {

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

        @SerializedName("movement_id")
        @Expose
        private String movementId;
        @SerializedName("from_crate_id")
        @Expose
        private String fromCrateId;
        @SerializedName("to_crate_id")
        @Expose
        private String toCrateId;
        @SerializedName("date")
        @Expose
        private String date;
        @SerializedName("time")
        @Expose
        private String time;
        @SerializedName("from_crate_name")
        @Expose
        private String fromCrateName;
        @SerializedName("to_crate_name")
        @Expose
        private String toCrateName;
        @SerializedName("item_total")
        @Expose
        private String item_total;
        @SerializedName("item_data")
        @Expose
        private List<ItemData> itemData = null;

        public String getMovementId() {
            return movementId;
        }

        public void setMovementId(String movementId) {
            this.movementId = movementId;
        }

        public String getFromCrateId() {
            return fromCrateId;
        }

        public void setFromCrateId(String fromCrateId) {
            this.fromCrateId = fromCrateId;
        }

        public String getToCrateId() {
            return toCrateId;
        }

        public void setToCrateId(String toCrateId) {
            this.toCrateId = toCrateId;
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

        public String getFromCrateName() {
            return fromCrateName;
        }

        public void setFromCrateName(String fromCrateName) {
            this.fromCrateName = fromCrateName;
        }

        public String getToCrateName() {
            return toCrateName;
        }

        public void setToCrateName(String toCrateName) {
            this.toCrateName = toCrateName;
        }

        public List<ItemData> getItemData() {
            return itemData;
        }

        public void setItemData(List<ItemData> itemData) {
            this.itemData = itemData;
        }

        public String getItem_total() {
            return item_total;
        }

        public void setItem_total(String item_total) {
            this.item_total = item_total;
        }
    }

    public class ItemData {

        @SerializedName("move_item_qty")
        @Expose
        private String moveItemQty;
        @SerializedName("item_name")
        @Expose
        private String itemName;
        @SerializedName("item_id")
        @Expose
        private String itemId;

        public String getMoveItemQty() {
            return moveItemQty;
        }

        public void setMoveItemQty(String moveItemQty) {
            this.moveItemQty = moveItemQty;
        }

        public String getItemName() {
            return itemName;
        }

        public void setItemName(String itemName) {
            this.itemName = itemName;
        }

        public String getItemId() {
            return itemId;
        }

        public void setItemId(String itemId) {
            this.itemId = itemId;
        }

    }
}

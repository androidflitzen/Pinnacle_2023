package com.flitzen.pinnacle.inventory_management.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class PendingOrderBOMListModel {

    @SerializedName("order_no")
    @Expose
    private String orderNo;
    @SerializedName("item_name")
    @Expose
    private String itemName;
    @SerializedName("order_date")
    @Expose
    private String orderDate;
    @SerializedName("order_time")
    @Expose
    private String orderTime;
    @SerializedName("data")
    @Expose
    private List<Data> data = null;
    @SerializedName("status")
    @Expose
    private Integer status;
    @SerializedName("message")
    @Expose
    private String message;

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(String orderDate) {
        this.orderDate = orderDate;
    }

    public String getOrderTime() {
        return orderTime;
    }

    public void setOrderTime(String orderTime) {
        this.orderTime = orderTime;
    }

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

        @SerializedName("category")
        @Expose
        private String category;
        @SerializedName("data")
        @Expose
        private List<BOMDetails> data = null;

        public String getCategory() {
            return category;
        }

        public void setCategory(String category) {
            this.category = category;
        }

        public List<BOMDetails> getData() {
            return data;
        }

        public void setData(List<BOMDetails> data) {
            this.data = data;
        }

    }

    public class BOMDetails {

        @SerializedName("bom_id")
        @Expose
        private String bomId;
        @SerializedName("bom_product_id")
        @Expose
        private String bom_product_id;
        @SerializedName("bom_code")
        @Expose
        private String bomCode;
        @SerializedName("bom_name")
        @Expose
        private String bomName;
        @SerializedName("bom_is_decimal")
        @Expose
        private String bomIsDecimal;
        @SerializedName("bom_require_qty")
        @Expose
        private String bomRequireQty;
        @SerializedName("bom_stock_qty")
        @Expose
        private String bomStockQty;
        @SerializedName("bom_deliver_qty")
        @Expose
        private String bomDeliverQty;

        public String getBomIsDecimal() {
            return bomIsDecimal;
        }

        public void setBomIsDecimal(String bomIsDecimal) {
            this.bomIsDecimal = bomIsDecimal;
        }

        public String getBomId() {
            return bomId;
        }

        public void setBomId(String bomId) {
            this.bomId = bomId;
        }

        public String getBomCode() {
            return bomCode;
        }

        public void setBomCode(String bomCode) {
            this.bomCode = bomCode;
        }

        public String getBomName() {
            return bomName;
        }

        public void setBomName(String bomName) {
            this.bomName = bomName;
        }

        public String getBomRequireQty() {
            return bomRequireQty;
        }

        public void setBomRequireQty(String bomRequireQty) {
            this.bomRequireQty = bomRequireQty;
        }

        public String getBomStockQty() {
            return bomStockQty;
        }

        public void setBomStockQty(String bomStockQty) {
            this.bomStockQty = bomStockQty;
        }

        public String getBomDeliverQty() {
            return bomDeliverQty;
        }

        public void setBomDeliverQty(String bomDeliverQty) {
            this.bomDeliverQty = bomDeliverQty;
        }

        public String getBom_product_id() {
            return bom_product_id;
        }

        public void setBom_product_id(String bom_product_id) {
            this.bom_product_id = bom_product_id;
        }
    }

}

package com.flitzen.pinnacle.inventory_management.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class DeliveredOrderListModel {
    @SerializedName("data")
    @Expose
    private List<Result> data = null;
    @SerializedName("status")
    @Expose
    private Integer status;
    @SerializedName("message")
    @Expose
    private String message;

    public List<Result> getData() {
        return data;
    }

    public void setData(List<Result> data) {
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

    public class OrderItem {

        @SerializedName("machine_id")
        @Expose
        private String machineId;
        @SerializedName("machine_name")
        @Expose
        private String machineName;
        @SerializedName("machine_qty")
        @Expose
        private String machineQty;

        public String getMachineId() {
            return machineId;
        }

        public void setMachineId(String machineId) {
            this.machineId = machineId;
        }

        public String getMachineName() {
            return machineName;
        }

        public void setMachineName(String machineName) {
            this.machineName = machineName;
        }

        public String getMachineQty() {
            return machineQty;
        }

        public void setMachineQty(String machineQty) {
            this.machineQty = machineQty;
        }


    }

    public class Result {

        @SerializedName("order_no")
        @Expose
        private String orderNo;
        @SerializedName("customer_name")
        @Expose
        private String customerName;
        @SerializedName("order_item")
        @Expose
        private List<OrderItem> orderItem = null;
        @SerializedName("order_date")
        @Expose
        private String orderDate;
        @SerializedName("order_time")
        @Expose
        private String orderTime;

        public String getOrderNo() {
            return orderNo;
        }

        public void setOrderNo(String orderNo) {
            this.orderNo = orderNo;
        }

        public String getCustomerName() {
            return customerName;
        }

        public void setCustomerName(String customerName) {
            this.customerName = customerName;
        }

        public List<OrderItem> getOrderItem() {
            return orderItem;
        }

        public void setOrderItem(List<OrderItem> orderItem) {
            this.orderItem = orderItem;
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

    }
}

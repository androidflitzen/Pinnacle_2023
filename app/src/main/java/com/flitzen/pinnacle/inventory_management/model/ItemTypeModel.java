package com.flitzen.pinnacle.inventory_management.model;

public class ItemTypeModel {
    String itemName;
    int itemPartCount;

    public ItemTypeModel(String itemName, int itemPartCount) {
        this.itemName = itemName;
        this.itemPartCount = itemPartCount;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public int getItemPartCount() {
        return itemPartCount;
    }

    public void setItemPartCount(int itemPartCount) {
        this.itemPartCount = itemPartCount;
    }
}

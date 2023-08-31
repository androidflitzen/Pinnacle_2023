package com.flitzen.pinnacle.inventory_management.model;

public class CraeteItemModel {
    String id;
    String qty;
    String name;

    public CraeteItemModel(String id, String qty,String name) {
        this.id = id;
        this.qty = qty;
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getQty() {
        return qty;
    }

    public void setQty(String qty) {
        this.qty = qty;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}

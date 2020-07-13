package com.example.todolistproject;

public class ItemModel {
    String itemName;

    public ItemModel(){}

    public ItemModel(String itemName) {
        this.itemName = itemName;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }
}

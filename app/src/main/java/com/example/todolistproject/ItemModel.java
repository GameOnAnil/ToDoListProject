package com.example.todolistproject;

public class ItemModel {
    String item;
    String date;
    String time;

    public ItemModel(){}

    public ItemModel(String item, String date, String time) {
        this.item = item;
        this.date = date;
        this.time = time;
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

    public String getItem() {
        return item;
    }

    public void setItem(String item) {
        this.item = item;
    }
}

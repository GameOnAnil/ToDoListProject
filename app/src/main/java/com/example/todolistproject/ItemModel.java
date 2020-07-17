package com.example.todolistproject;

public class ItemModel {
    String item;
    String date;
    String time;
    Boolean completed;

    public ItemModel(){}

    public ItemModel(String item, String date, String time,Boolean completed) {
        this.item = item;
        this.date = date;
        this.time = time;
        this.completed = completed;
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

    public Boolean getCompleted() {
        return completed;
    }

    public void setCompleted(Boolean completed) {
        this.completed = completed;
    }
}

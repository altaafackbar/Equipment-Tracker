package com.example.geartracker;

import android.graphics.Bitmap;

public class Gear {
    private String date;
    private String maker;
    private String description;
    private double price;
    private String comment;
    private byte[] image;

    public Gear(String date, String maker, String description, double price, String comment, byte[] image) {
        this.date = date;
        this.maker = maker;
        this.description = description;
        this.price = price;
        this.comment = comment;
        this.image = image;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getMaker() {
        return maker;
    }

    public void setMaker(String maker) {
        this.maker = maker;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }
}

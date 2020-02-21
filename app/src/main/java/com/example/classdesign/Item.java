package com.example.classdesign;

public class Item {
    private String itemText;
    private int itemPicture;

    public Item(String itemText,int itemPicture){
        this.itemText = itemText;
        this.itemPicture = itemPicture;
    }

    public String getItemText() {
        return itemText;
    }
    public int getItemPicture() {
        return itemPicture;
    }
}

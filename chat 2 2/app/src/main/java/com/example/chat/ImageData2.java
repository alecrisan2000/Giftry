package com.example.chat;

import android.graphics.Bitmap;

public class ImageData2 {
    private String userEmail;
    private Bitmap image;
    private String giftName;

    public ImageData2(String userEmail, Bitmap image, String giftName) {
        this.userEmail = userEmail;
        this.image = image;
        this.giftName = giftName;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public Bitmap getImage() {
        return image;
    }

    public String getGiftName() {
        return giftName;
    }

}


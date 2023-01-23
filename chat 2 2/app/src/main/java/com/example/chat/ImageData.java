package com.example.chat;

import android.graphics.Bitmap;

class ImageData {
    private Bitmap image;
    private String giftName;

    public ImageData(Bitmap image, String giftName) {
        this.image = image;
        this.giftName = giftName;
    }

    public Bitmap getImage() {
        return image;
    }

    public String getGiftName() {
        return giftName;
    }
}


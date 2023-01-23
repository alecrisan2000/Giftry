package com.example.chat;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class ImageAdapter2 extends BaseAdapter {
    private List<ImageData2> imagesData;
    private Context context;

    public ImageAdapter2(List<ImageData2> imagesData, Context context) {
        this.imagesData = imagesData;
        this.context = context;
    }

    @Override
    public int getCount() {
        return imagesData.size();
    }

    @Override
    public Object getItem(int position) {
        return imagesData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.feed_item, parent, false);
        }

        ImageData2 data = imagesData.get(position);
        ImageView imageView = convertView.findViewById(R.id.imageView);
        TextView giftNameTextView = convertView.findViewById(R.id.nameTextView);
        TextView emailTextView = convertView.findViewById(R.id.emailTextView);

        imageView.setImageBitmap(data.getImage());
        giftNameTextView.setText(data.getGiftName());
        emailTextView.setText(data.getUserEmail());

        return convertView;
    }
}



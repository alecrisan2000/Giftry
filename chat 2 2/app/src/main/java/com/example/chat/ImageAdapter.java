package com.example.chat;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class ImageAdapter extends BaseAdapter {
    private List<Bitmap> images;
    private List<String> giftNames;
    private Context context;
    private ImageButton deleteButton;
    private Connection connection;

    public ImageAdapter(List<Bitmap> images, List<String> giftNames, Context context) {
        this.images = images;
        this.giftNames = giftNames;
        this.context = context;
    }

    @Override
    public int getCount() {
        return images.size();
    }

    @Override
    public Object getItem(int position) {
        return images.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.grid_item, parent, false);
        }

        ImageView imageView = convertView.findViewById(R.id.grid_item_image);
        Button deleteButton = convertView.findViewById(R.id.deleteButton);
        TextView textView = convertView.findViewById(R.id.grid_item_gift_name);
        imageView.setImageBitmap(images.get(position));
        textView.setText(giftNames.get(position));
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                images.remove(position);
                giftNames.remove(position);
                notifyDataSetChanged();
            }
        });

        return convertView;
    }


    private class DeleteImageTask extends AsyncTask<Integer, Void, Void> {
        private int position;

        @Override
        protected Void doInBackground(Integer... integers) {
            position = integers[0];
            try {
              //  String user_email = getCurrentUserEmail();
                String user_email = "a";
                try {
                    connection = ConnectionHelper.getConnection().get();
                } catch (ExecutionException | InterruptedException e) {
                    e.printStackTrace();
                }

                PreparedStatement statement = connection.prepareStatement("DELETE FROM images WHERE user_email = ? and gift_name = ?");
                statement.setString(1, user_email);
                statement.setString(2, giftNames.get(position));
                statement.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return null;
        }
    }
    public void onDeleteButtonClick(View view) {
        int position = (Integer) view.getTag();
        new DeleteImageTask().execute(position);
    }
}


//public class ImageAdapter extends BaseAdapter {
//    private List<Bitmap> images;
//    private List<String> giftNames;
//    private Context context;
//
//    public ImageAdapter(List<Bitmap> images, List<String> giftNames, Context context) {
//        this.images = images;
//        this.giftNames = giftNames;
//        this.context = context;
//    }
//
//    @Override
//    public int getCount() {
//        return images.size();
//    }
//
//    @Override
//    public Object getItem(int position) {
//        return images.get(position);
//    }
//
//    @Override
//    public long getItemId(int position) {
//        return position;
//    }
//
//    @Override
//    public View getView(int position, View convertView, ViewGroup parent) {
//        View view = convertView;
//        if (view == null) {
//            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//         //   view = inflater.inflate(R.layout.image_item, null);
//            view = inflater.inflate(R.layout.grid_item, null);
//
//        }
//      //  ImageView imageView = view.findViewById(R.id.imageView);
//        ImageView imageView = view.findViewById(R.id.grid_item_image);
//        TextView textView = view.findViewById(R.id.grid_item_gift_name);
//    //    TextView textView = view.findViewById(R.id.textView_gift_name);
//
//        imageView.setImageBitmap(images.get(position));
//        textView.setText(giftNames.get(position));
//        return view;
//    }
//
//
//    }









//public class ImageAdapter extends BaseAdapter {
//
//    private List<Bitmap> imageData;
//    private Context context;
//
//    public ImageAdapter(List<Bitmap> imageData, Context context) {
//        this.imageData = imageData;
//        this.context = context;
//    }
//
//    @Override
//    public int getCount() {
//        return imageData.size();
//    }
//
//    @Override
//    public Object getItem(int position) {
//        return imageData.get(position);
//    }
//
//    @Override
//    public long getItemId(int position) {
//        return position;
//    }
//
//    @Override
//    public View getView(int position, View convertView, ViewGroup parent) {
//        ImageView imageView;
//        if (convertView == null) {
//            imageView = new ImageView(context);
//            imageView.setLayoutParams(new GridView.LayoutParams(200, 200));
//            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
//            imageView.setPadding(8, 8, 8, 8);
//        } else {
//            imageView = (ImageView) convertView;
//        }
//        Bitmap image = (Bitmap) getItem(position);
//        imageView.setImageBitmap(image);
//
//        return imageView;
//    }
//}
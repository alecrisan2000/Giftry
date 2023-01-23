package com.example.chat;


import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class FeedActivity extends AppCompatActivity {

    private ListView listView;
    private ImageAdapter2 imageAdapter;
    private Connection connection;
    private List<ImageData2> imageDataList = new ArrayList<>();
    private Button profileButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed);
        listView = findViewById(R.id.feedListView);
        profileButton = findViewById(R.id.profileButton);

        // Get the database connection
        try {
            connection = ConnectionHelper.getConnection().get();
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
        if (connection == null) {
            System.out.println("Null connection");
            return;
        }

        profileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(FeedActivity.this, ProfileActivity.class);
                startActivity(intent);
            }
        });
        // Get the images and their corresponding gift names and user emails from the database and display them in the GridView
        new GetImagesTask().execute();
    }

    private class GetImagesTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... voids) {
            try {
                String currentUserEmail = getCurrentUserEmail();
                PreparedStatement statement = connection.prepareStatement("SELECT image, gift_name, user_email FROM images WHERE user_email != ?");
                statement.setString(1, currentUserEmail);ResultSet resultSet = statement.executeQuery();
                while (resultSet.next()) {
                    byte[] image = resultSet.getBytes("image");
                    String giftName = resultSet.getString("gift_name");
                    String userEmail = resultSet.getString("user_email");
                    Bitmap bitmap = BitmapFactory.decodeByteArray(image, 0, image.length);
                    ImageData2 imageData = new ImageData2(userEmail,bitmap, giftName);
                    imageDataList.add(imageData);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return null;
        }

        private String getCurrentUserEmail() {
            // Example using shared preferences
            SharedPreferences preferences = getSharedPreferences("user_preferences", MODE_PRIVATE);
            return preferences.getString("user_email", "");
            //    return "a";

        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            imageAdapter = new ImageAdapter2(imageDataList, FeedActivity.this);
            listView.setAdapter(imageAdapter);
        }
    }


}


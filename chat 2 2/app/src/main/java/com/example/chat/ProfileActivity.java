package com.example.chat;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Pair;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class ProfileActivity extends AppCompatActivity implements GiftNameDialogFragment.GiftNameDialogListener {

    private TextView nameTextView;
    private GridView gridView;
    private ImageAdapter imageAdapter;
    private Connection connection;
    private Button addImageButton;
    private EditText giftNameEditText;
    private List<String> giftNames = new ArrayList<>();
    private List<Bitmap> images = new ArrayList<>();
    private Button logoutButton;
    private Button feedButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        nameTextView = findViewById(R.id.nameTextView);
        gridView = findViewById(R.id.gridView);
        addImageButton = findViewById(R.id.addImageButton);
        giftNameEditText = findViewById(R.id.giftNameEditText);
        logoutButton = findViewById(R.id.logoutButton);
        feedButton = findViewById(R.id.feedButton);
        // Get the current user's email
        String userEmail = getCurrentUserEmail();

        // Set the user's name in the TextView
        nameTextView.setText(userEmail);

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

        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                logout();
            }
        });

        feedButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Start the FeedActivity
                Intent intent = new Intent(ProfileActivity.this, FeedActivity.class);
                startActivity(intent);
            }
        });

        // Get the images from the database and display them in the GridView
        new GetImagesTask().execute();

        // Set the onClickListener for the addImageButton
        addImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showGiftNameDialog();
            }
        });
    }
    private void logout() {
        // Clear user information saved in shared preferences
        SharedPreferences preferences = getSharedPreferences("user_preferences", MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.clear();
        editor.apply();

        // Redirect user to login activity
        Intent intent = new Intent(ProfileActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }
    private void showGiftNameDialog() {
        GiftNameDialogFragment giftNameDialogFragment = new GiftNameDialogFragment();
        FragmentManager fm = getSupportFragmentManager();
        giftNameDialogFragment.show(fm, "gift_name_dialog");
    }

    @Override
    public void onFinishGiftNameDialog(String giftName) {
        // Use the giftName here
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri uri = data.getData();
            String giftName = giftNameEditText.getText().toString();
            new AddImageTask(uri, giftName).execute();
        }
    }


    private class AddImageTask extends AsyncTask<Void, Void, Void> {
        private Uri uri;
        private String giftName;

        public AddImageTask(Uri uri, String giftName) {
            this.uri = uri;
            this.giftName = giftName;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
                byte[] image = stream.toByteArray();
                String user_email = getCurrentUserEmail();
                PreparedStatement statement = connection.prepareStatement("INSERT INTO images (user_email, image, gift_name) VALUES (?,?,?)");
                statement.setString(1, user_email);
                statement.setBytes(2, image);
                statement.setString(3, giftName);
                statement.executeUpdate();


            } catch (IOException | SQLException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            new GetImagesTask().execute();
        }
    }

    private String getCurrentUserEmail() {
        // Example using shared preferences
//        SharedPreferences preferences = getSharedPreferences("user_preferences", MODE_PRIVATE);
//        return preferences.getString("user_email", "");
        return "alexandra";

    }

        private class GetImagesTask extends AsyncTask<Void, Void, Pair<List<Bitmap>, List<String>>> {

            @Override
            protected Pair<List<Bitmap>, List<String>> doInBackground(Void... voids) {
                try {
                    String user_email = getCurrentUserEmail();
                    PreparedStatement statement = connection.prepareStatement("SELECT image,gift_name FROM images WHERE user_email = ?");
                    statement.setString(1, user_email);
                    ResultSet resultSet = statement.executeQuery();
                    List<Bitmap> images = new ArrayList<>();
                    List<String> giftNames = new ArrayList<>();
                    while (resultSet.next()) {
                        byte[] image = resultSet.getBytes("image");
                        String gift_name = resultSet.getString("gift_name");
                        Bitmap bitmap = BitmapFactory.decodeByteArray(image, 0, image.length);
                        images.add(bitmap);
                        giftNames.add(gift_name);

                    }
                    return new Pair<>(images, giftNames);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            protected void onPostExecute(Pair<List<Bitmap>, List<String>> result) {
                if (result != null) {
                    imageAdapter = new ImageAdapter(result.first, result.second, ProfileActivity.this);
                    gridView.setAdapter(imageAdapter);

                }

            }


        }


//    public void onDeleteButtonClick(View view) {
//        int position = gridView.getPositionForView(view);
//        deleteImage(position);
//    }
//    private void deleteImage(final int position) {
//        new DeleteImageTask(position).execute();
//    }
//
//    private class DeleteImageTask extends AsyncTask<Void, Void, Void> {
//
//        private int position;
//
//        public DeleteImageTask(int position) {
//            this.position = position;
//        }
//
//        @Override
//        protected Void doInBackground(Void... voids) {
//            try {
//                String user_email = getCurrentUserEmail();
//                PreparedStatement statement = connection.prepareStatement("DELETE FROM images WHERE user_email = ? AND gift_name = ?");
//                statement.setString(1, user_email);
//                statement.setString(2, giftNames.get(position));
//                statement.executeUpdate();
//            } catch (SQLException e) {
//                e.printStackTrace();
//            }
//            return null;
//        }
//
//        @Override
//        protected void onPostExecute(Void aVoid) {
//            super.onPostExecute(aVoid);
//            images.remove(position);
//            giftNames.remove(position);
//            gridView.invalidateViews();
//        }
//    }


}



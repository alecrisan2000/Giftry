package com.example.chat;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

import org.bson.Document;
//import com.mongodb.ServerApi;
//import com.mongodb.ServerApiVersion;


import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class SignUpActivity extends AppCompatActivity {

    private EditText mEmailEditText;
    private EditText mPasswordEditText;
    private EditText mNameEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        mEmailEditText = findViewById(R.id.email_edit_text);
        mPasswordEditText = findViewById(R.id.password_edit_text);
        mNameEditText = findViewById(R.id.name_edit_text);
        Button signUpButton = findViewById(R.id.signup_button);
        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = mEmailEditText.getText().toString();
                String password = mPasswordEditText.getText().toString();
                String name = mNameEditText.getText().toString();
                new SignUpTask().execute(email, password, name);
            }
        });
    }

    class SignUpTask extends AsyncTask<String, Void, Boolean> {
        @Override
        protected Boolean doInBackground(String... params) {
            String email = params[0];
            String password = params[1];
            String name = params[2];

            try {
                // Register the driver
                Class.forName("org.postgresql.Driver");

                // Open a connection
                String url = "jdbc:postgresql://10.0.2.2:5432/alexandracrisan";
                Connection con = DriverManager.getConnection(url, "alexandracrisan", "postgres");

                // Execute the query
                String query = "INSERT INTO users (email, password, name) VALUES (?, ?, ?)";
                PreparedStatement st = con.prepareStatement(query);
                st.setString(1, email);
                st.setString(2, password);
                st.setString(3, name);
                st.executeUpdate();

                // Close the connection
                con.close();
                return true;
            } catch (ClassNotFoundException | SQLException e) {
                e.printStackTrace();
                return false;
            }
        }

        @Override
        protected void onPostExecute(Boolean success) {
            super.onPostExecute(success);
            if (success) {
                Toast.makeText(SignUpActivity.this, "User created successfully!", Toast.LENGTH_SHORT).show();
                // Navigate to the next activity or show a success message
                Intent intent = new Intent(SignUpActivity.this, MainActivity.class);
                startActivity(intent);
            } else {
                Toast.makeText(SignUpActivity.this, "Error creating user, please try again later!", Toast.LENGTH_SHORT).show();
                // Show an error message
            }
        }
    }


}
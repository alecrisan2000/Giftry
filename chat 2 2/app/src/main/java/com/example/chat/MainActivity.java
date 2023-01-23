package com.example.chat;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.sql.SQLException;
import java.util.Collections;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class MainActivity extends AppCompatActivity {
    private EditText emailEditText;
    private EditText passwordEditText;
    private Button loginButton;
    private Button signupButton;


    // @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        emailEditText = (EditText) findViewById(R.id.emailEditText);
        passwordEditText = (EditText) findViewById(R.id.passwordEditText);
        loginButton = (Button) findViewById(R.id.loginButton);
        signupButton = (Button) findViewById(R.id.signUpButton);


            loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = emailEditText.getText().toString();
                String password = passwordEditText.getText().toString();
                // Perform login action with email and password
                new MainActivity.SignUpTask().execute(email, password);
            }
        });
            signupButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(MainActivity.this, SignUpActivity.class);
                    startActivity(intent);
                }
            });
    }
    private class SignUpTask extends AsyncTask<String, Void, Boolean> {
      //  public boolean login(String email, String password) {
      protected Boolean doInBackground(String... params){
          String email = params[0];
          String password = params[1];
        boolean status = false;

            try {
                // Register the driver
                Class.forName("org.postgresql.Driver");

                // Open a connection
                String url = "jdbc:postgresql://10.0.2.2:5432/alexandracrisan";
                Connection con = DriverManager.getConnection(url, "alexandracrisan", "postgres");

                PreparedStatement ps = con.prepareStatement("SELECT * FROM users WHERE email = ? AND password = ?");
                ps.setString(1, email);
                ps.setString(2, password);
                ResultSet rs = ps.executeQuery();
                status = rs.next();

                con.close();
            } catch (ClassNotFoundException | SQLException e) {
                e.printStackTrace();
            }

            return status;
        }
        protected void onPostExecute(Boolean success) {
            super.onPostExecute(success);
            if (success) {
                Toast.makeText(MainActivity.this, "Login Successful!", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(MainActivity.this, FeedActivity.class);
                startActivity(intent);
            } else {
                Toast.makeText(MainActivity.this, "Invalid username or password!", Toast.LENGTH_SHORT).show();
                // Show an error message
            }
        }
    }

}

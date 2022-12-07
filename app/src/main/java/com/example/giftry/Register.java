package com.example.giftry;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseUser;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.parse.SignUpCallback;

public class Register extends Activity implements register1 {
    Button b1,b2;
    EditText ed1,ed2;
    //hiuhuhutgerr
    TextView tx1;

    ParseUser user = new ParseUser();
    // Set the user's username and password, which can be obtained by a forms
        user.setUsername( "<Your username here>");
        user.setPassword( "<Your password here>");
        user.signUpInBackground(new SignUpCallback() {
        @Override
        public void done(ParseException e) {
            if (e == null) {
                showAlert("Successful Sign Up!", "Welcome" + "<Your username here>" +"!");
            } else {
                ParseUser.logOut();
                Toast.makeText(SignUpActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
            }
        }
    });
//    protected void Register(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        b1.setOnClickListener(new View.OnClickListener() {
//
//            public void onClick(View view) {
//
//            }
//        });
//        protected void Register(Bundle savedInstanceState){
//            b2.setOnClickListener(new View.OnClickListener() {
//
//                public void onClick(View v) {
//                    finish();
//                }
//            });
//        }
//    }
}

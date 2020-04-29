package com.turnyur.taskscheduler;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import activity.MainActivity;

public class Login extends AppCompatActivity {

    EditText username,mypassword;
    FloatingActionButton login_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        username= (EditText) findViewById(R.id.user_name);
        mypassword= (EditText) findViewById(R.id.pass_word);
        login_btn= (FloatingActionButton) findViewById(R.id.button);
        login_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String user_check=username.getText().toString();
                String pass_check=mypassword.getText().toString();
                if((user_check.equals("Admin")||user_check.equals("admin")) && pass_check.equals("admin")){
                    Intent i=new Intent(Login.this, MainActivity.class);
                    startActivity(i);
                    Toast.makeText(getApplicationContext(),
                            "Welcome, "+username.getText().toString(), Toast.LENGTH_LONG).show();
                }else{
                    Toast.makeText(getApplicationContext(),
                            "Incorrect username or password", Toast.LENGTH_LONG).show();
                    username.setText("");
                    mypassword.setText("");

                }
            }
        });





    }






    }


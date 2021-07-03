package com.example.whack_a_mole_30;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class RegisterActivity extends AppCompatActivity {
    EditText username;
    EditText password;
    Button cancel;
    Button create;
    DBHandler dbHandler = new DBHandler(this,null,null,1);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        username = findViewById(R.id.UsernameEdit);
        password = findViewById(R.id.PasswordEdit);
        cancel = findViewById(R.id.CancelButton);
        create = findViewById(R.id.CreateButton);

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });

        create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dbHandler.createAccount(username.getText().toString(),password.getText().toString(),RegisterActivity.this);
            }
        });
    }
}
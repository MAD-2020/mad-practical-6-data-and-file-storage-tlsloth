package com.example.whack_a_mole_30;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    EditText username;
    EditText password;
    Button Loginbutton;
    TextView Register;
    DBHandler dbHandler = new DBHandler(this,null,null,1);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        username = findViewById(R.id.UsernameEdit);
        password = findViewById(R.id.PasswordEdit);
        Loginbutton = findViewById(R.id.LoginButton);
        Register = findViewById(R.id.CreateAccountText);

        Loginbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(dbHandler.loginAccount(username.getText().toString(),password.getText().toString(),MainActivity.this)){
                    //intent to new page
                    Intent intent = new Intent(MainActivity.this,SelectLevel.class);
                    startActivity(intent);
                }
            }
        });
        Register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //intent to new page
                Intent intent = new Intent(MainActivity.this,RegisterActivity.class);
                startActivity(intent);
            }
        });
    }
}
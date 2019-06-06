package com.example.husiapp2;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.wifi.WifiManager;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {

    EditText email, password;
    TextView warning,login;
    private FirebaseAuth mAuth;
    Button b1,b2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        email = findViewById(R.id.Email);
        login = findViewById(R.id.LoginLabel);
        password = findViewById(R.id.Password);
        mAuth = FirebaseAuth.getInstance();
        b1 = findViewById(R.id.RegisterButton);
        b2 = findViewById(R.id.LoginButton);
        warning = findViewById(R.id.Warning);
        WifiManager wifi = (WifiManager)getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        if(!wifi.isWifiEnabled()){
            login.setVisibility(View.GONE);
            email.setVisibility(View.GONE);
            password.setVisibility(View.GONE);
            b1.setVisibility(View.GONE);
            b2.setVisibility(View.GONE);
            warning.setText("To use this app, consider establishing an internet connection");
        }
    }

    public void GoToRegister(View view){
        startActivity(new Intent(MainActivity.this, RegisterActivity.class));
    }
    public void Login(View view){
            final String mail = email.getText().toString();
            final String pword = password.getText().toString();
            if(!TextUtils.isEmpty(mail) || !TextUtils.isEmpty(pword)){
                mAuth.signInWithEmailAndPassword(mail,pword).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            startActivity(new Intent(MainActivity.this,HomeActivity.class));
                        }else {
                            Toast.makeText(MainActivity.this, "Signin failed", Toast.LENGTH_SHORT).show();
                            email.setHintTextColor(Color.GRAY);
                            password.setHintTextColor(Color.GRAY);
                        }
                    }
                });
            }else {
                Toast.makeText(this, "Field can't be empty", Toast.LENGTH_SHORT).show();
                email.setHintTextColor(Color.RED);
                password.setHintTextColor(Color.RED);
            }
        }


    }


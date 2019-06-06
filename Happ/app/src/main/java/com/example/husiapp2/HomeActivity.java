package com.example.husiapp2;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class HomeActivity extends AppCompatActivity {
    TextView message;
    private FirebaseAuth mAuth;
    FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        mAuth = FirebaseAuth.getInstance();
        message = findViewById(R.id.AboutUser);
        user = mAuth.getCurrentUser();
        message.setText( "Welcome back " + user.getEmail());
    }
    public void Logout(View view){
        mAuth.signOut();
        startActivity(new Intent(HomeActivity.this,MainActivity.class));
    }

    @Override
    protected void onStart() {
        super.onStart();
        if(mAuth.getCurrentUser() == null){
            startActivity(new Intent(HomeActivity.this,MainActivity.class));
        }
    }
    public void AddHomework(View view){
        startActivity(new Intent(this,AddHwActivity.class));
    }
    public void ViewHomework(View view){
        startActivity(new Intent(this,ViewHwActivity.class));
    }

}

package com.example.husiapp2;

import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

public class RegisterActivity extends AppCompatActivity {

    EditText firstname,lastname,password,email;
    private FirebaseAuth mAuth;
    User user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        firstname = findViewById(R.id.Lastname);
        lastname = findViewById(R.id.Firstname);
        email = findViewById(R.id.Email);
        password = findViewById(R.id.Password);
        mAuth = FirebaseAuth.getInstance();
    }

    public void Register(View view){
        final String fname = firstname.getText().toString();
        final String lname = lastname.getText().toString();
        final String pword = password.getText().toString();
        final String mail = email.getText().toString();

        if(TextUtils.isEmpty(fname) || TextUtils.isEmpty(lname) || TextUtils.isEmpty(pword) || TextUtils.isEmpty(mail)){
            Toast.makeText(this, "Fields can't be empty",Toast.LENGTH_LONG).show();
            firstname.setHintTextColor(Color.RED);
            lastname.setHintTextColor(Color.RED);
            email.setHintTextColor(Color.RED);
            password.setHintTextColor(Color.RED);

        }else {
            mAuth.createUserWithEmailAndPassword(mail,pword).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful()){
                        user = new User(fname,lname,pword,mail);
                        FirebaseDatabase.getInstance().getReference("User").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if(task.isSuccessful()){
                                    Toast.makeText(RegisterActivity.this, "User created", Toast.LENGTH_SHORT).show();
                                    startActivity(new Intent(RegisterActivity.this,MainActivity.class));
                                }else {
                                    Toast.makeText(RegisterActivity.this, "User not created", Toast.LENGTH_SHORT).show();
                                    firstname.setHintTextColor(Color.GRAY);
                                    lastname.setHintTextColor(Color.GRAY);
                                    email.setHintTextColor(Color.GRAY);
                                    password.setHintTextColor(Color.GRAY);
                                }
                            }
                        });

                    }else {
                        Toast.makeText(RegisterActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                        firstname.setHintTextColor(Color.GRAY);
                        lastname.setHintTextColor(Color.GRAY);
                        email.setHintTextColor(Color.GRAY);
                        password.setHintTextColor(Color.GRAY);
                    }
                }
            });
        }

    }
}

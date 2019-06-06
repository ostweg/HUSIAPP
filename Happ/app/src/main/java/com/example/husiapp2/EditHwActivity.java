package com.example.husiapp2;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;

import java.util.HashMap;
import java.util.Map;

public class EditHwActivity extends AppCompatActivity {
    EditText t1,t2,t3,t4;
    Gson gson = new Gson();
    Homework homework;
    DatabaseReference ref = FirebaseDatabase.getInstance().getReference("User");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_hw);
        t1 = findViewById(R.id.Title);
        t2 = findViewById(R.id.Class);
        t3 = findViewById(R.id.ExpireDate);
        t4 = findViewById(R.id.DateToNotify);
        SetHomeWork();
    }
    public void SetHomeWork(){
        Intent i = getIntent();
        Bundle b = i.getBundleExtra("Person");
        String name = b.getString("Homework");
        homework = gson.fromJson(name,Homework.class);
        t1.setText(homework.Title);
        t2.setText(homework.Class);
        t3.setText(homework.ExpireDate);
        t4.setText(homework.DateToNotify);
    }
    public void DeleteItem(View view){
        Query item = ref.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("Homework").orderByChild("Title").equalTo(homework.Title);
        item.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot ds: dataSnapshot.getChildren()){
                    Toast.makeText(EditHwActivity.this,"Item has been deleted", Toast.LENGTH_LONG).show();
                    ds.getRef().removeValue();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.d("EditHWActivity","oncancelled",databaseError.toException());
            }
        });
    }
    public void UpdateItem(View view){
        final Map<String,Object> map = new HashMap<>();
        map.put("Title",t1.getText().toString());
        map.put("Class",t2.getText().toString());

        Query item2 = ref.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("Homework").orderByChild("Title").equalTo(homework.Title);
        Query item3 = ref.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("Homework").orderByChild("Class").equalTo(homework.Class);
        item2.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot ds: dataSnapshot.getChildren()){
                    ds.getRef().updateChildren(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                                Toast.makeText(EditHwActivity.this,"Item has been updated",Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.d("EditHwActivity","update went wrong");
            }
        });
        item3.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot ds: dataSnapshot.getChildren()){
                    ds.getRef().updateChildren(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                                Toast.makeText(EditHwActivity.this,"Item has been updated",Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.d("EditHwActivity","update went wrong");
            }
        });
    }
}

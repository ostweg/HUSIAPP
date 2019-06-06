package com.example.husiapp2;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebHistoryItem;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

public class ViewHwActivity extends AppCompatActivity {

    DatabaseReference ref;
    Homework homework;
    List<Homework> hw = new ArrayList<>();
    WebHistoryItem manager;
    Long homeworks;

    ListView hwlist;
    public ViewHwActivity(){

    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_hw);
        hwlist = findViewById(R.id.hwlist);
        ref = FirebaseDatabase.getInstance().getReference("User").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("Homework");
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot ds:dataSnapshot.getChildren()){
                    homework = new Homework();
                    homework.Title = ds.getValue(Homework.class).Title;
                    homework.Class = ds.getValue(Homework.class).Class;
                    homework.ExpireDate = ds.getValue(Homework.class).ExpireDate;
                    homework.DateToNotify = ds.getValue(Homework.class).DateToNotify;
                    hw.add(homework);
                }
                SetAdapter();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }

        });


    }
    public void SetAdapter(){
        ArrayAdapter adapter = new ArrayAdapter<Homework>(this,android.R.layout.simple_list_item_1,hw);
        hwlist.setAdapter(adapter);
        hwlist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent i = new Intent(ViewHwActivity.this,EditHwActivity.class);
                Bundle b = new Bundle();
                Gson gson = new Gson();
                b.putString("Homework",gson.toJson(ReturnHomework(id)));
                i.putExtra("Person",b);
                startActivity(i);
            }
        });
    }
    public Homework ReturnHomework(long Hid){
        return hw.get((int)Hid);
    }


}

package com.example.husiapp2;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.job.JobParameters;
import android.app.job.JobService;
import android.media.MediaPlayer;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class DeleteJobService extends JobService {
    public String DateToCompare;
    AddHwActivity hwa;
    public DeleteJobService(){

    }
    private static final String TAG = "DeleteJobService";
    private static final String Channel_ID = "com.example.husiapp2.ANDROID";
    DatabaseReference ref = FirebaseDatabase.getInstance().getReference("User");
    private boolean jobCancelled = false;
    static String Title;
    private boolean isWorking = false;
    static String Date;
    @Override
    public boolean onStartJob(JobParameters jobParameters) {
        Log.d(TAG,  "Delete Job started");
        DoWork(jobParameters);
        isWorking = false;
        return isWorking;
    }

    @Override
    public boolean onStopJob(JobParameters jobParameters) {
        Log.d(TAG, "Job cancelled");
        jobCancelled = true;
        boolean Reschedule = isWorking;
        return Reschedule;
    }
    public static boolean IsExpired(String date,String title){
        Date = date;
        Title = title;
        Boolean IsValid = false;
        Date ExpireDate = null;
        String format1 = "MM/dd/yy hh:mm";
        SimpleDateFormat sdf1 = new SimpleDateFormat(format1, Locale.GERMANY);
        try {
            ExpireDate = sdf1.parse(Date);
            if(ExpireDate.equals(new Date())){
                IsValid = true;
            }
            else {
                IsValid = false;
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }


        return IsValid;
    }
    private void DoWork(final JobParameters param){
        new Thread(new Runnable() {
            @Override
            public void run() {
                    if(jobCancelled){
                        return;
                    }
                    else{
                        String date1 = Date;
                        String Title1 = Title;
                        if(IsExpired(date1,null)){
                            Log.d(TAG,"Homework removed");
                            jobCancelled = true;
                            jobFinished(param, false);
                            DeleteItem(Title1);
                        }
                        else{
                            Log.d(TAG,"test");
                            return;
                        }
                    }

                Log.d(TAG,"Job finished");
            }
        }).start();
    }
    public void DeleteItem(String Title){
        Query item = ref.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("Homework").orderByChild("Title").equalTo(Title);
        item.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot ds: dataSnapshot.getChildren()){
                    Toast.makeText(DeleteJobService.this,"Item has been deleted", Toast.LENGTH_LONG).show();
                    ds.getRef().removeValue();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.d("EditHWActivity","oncancelled",databaseError.toException());
            }
        });
    }

}

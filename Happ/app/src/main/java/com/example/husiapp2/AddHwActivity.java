package com.example.husiapp2;

import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.app.AlarmManager;
import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class AddHwActivity extends AppCompatActivity {

    public AddHwActivity(){}
    EditText Title,Class, ExpireDate, DateToNotify;
    final Calendar calendar = Calendar.getInstance();
    final Calendar calendar1 = Calendar.getInstance();
    Homework homework;
    String date_time;
    Calendar cal;
    private static final String TAG = "AddHwActivity";
    private static final String Channel_ID ="test";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_hw);
        Title = findViewById(R.id.TitleOfHomework);
        Class = findViewById(R.id.ClassOfHomework);
        ExpireDate = findViewById(R.id.ExpireDateOfHomwork);
        DateToNotify = findViewById(R.id.NotificationDateOfHomework);

        final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                calendar.set(Calendar.YEAR, i);
                calendar.set(Calendar.MONTH,i1);
                calendar.set(Calendar.DAY_OF_MONTH,i2);
                UpdateLabel("expire");
            }

        };
        final DatePickerDialog.OnDateSetListener date1 = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                calendar1.set(Calendar.YEAR, i);
                calendar1.set(Calendar.MONTH,i1);
                calendar1.set(Calendar.DAY_OF_MONTH,i2);
                date_time = i1+"/"+i2+"/"+i;
                UpdateLabel("notfiy");
            }

        };
        final TimePickerDialog.OnTimeSetListener time = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int i, int i1) {
                calendar1.set(Calendar.HOUR_OF_DAY, i);
                calendar1.set(Calendar.MINUTE,i1);
            }
        };


        ExpireDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DatePickerDialog(AddHwActivity.this,date,calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH),calendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
        DateToNotify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DatePickerDialog(AddHwActivity.this,date1,calendar1.get(Calendar.YEAR), calendar1.get(Calendar.MONTH),calendar1.get(Calendar.DAY_OF_MONTH)).show();
                new TimePickerDialog(AddHwActivity.this,time,calendar1.get(Calendar.HOUR_OF_DAY), calendar1.get(Calendar.MINUTE),false).show();
            }
        });


    }

    public void UpdateLabel(String label){
        String format = "MM/dd/yy";
        String format1 = "MM/dd/yy hh:mm";
        SimpleDateFormat sdf = new SimpleDateFormat(format, Locale.GERMANY);
        SimpleDateFormat sdf1 = new SimpleDateFormat(format1, Locale.GERMANY);
        if(label == "expire"){
            ExpireDate.setText(sdf.format(calendar.getTime()));
        }
        else{
            Log.d(TAG,calendar1.getTime().toString());
            DateToNotify.setText(sdf1.format(calendar1.getTime()));
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void AddHomeWork(View view) throws ParseException {
        final String Htitle = Title.getText().toString();
        final String Hclass = Class.getText().toString();
        final String Hdate = ExpireDate.getText().toString();
        final String Hnotify = DateToNotify.getText().toString();
        final ComponentName comp = new ComponentName(this, DeleteJobService.class);

        if(!TextUtils.isEmpty(Htitle) || !TextUtils.isEmpty(Hclass) || !TextUtils.isEmpty(Hdate)){
            homework = new Homework(Htitle,Hclass,Hdate,Hnotify);
            DeleteJobService.IsExpired(Hdate,Htitle);
            FirebaseDatabase.getInstance().getReference("User").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("Homework").push().setValue(homework).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if(task.isSuccessful()){
                        Toast.makeText(AddHwActivity.this, "Homework added", Toast.LENGTH_SHORT).show();
                        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yy hh:mm");
                        try {
                            Date date = sdf.parse(Hnotify);
                            StartAlarm(date);
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        //Zgliche für Hdate mache
                        JobInfo info = new JobInfo.Builder(Htitle.length(),comp)
                                .setRequiredNetworkType(JobInfo.NETWORK_TYPE_UNMETERED)
                                .setPersisted(true)
                                .setPeriodic(60 * 60 * 1000)
                                .build();
                        JobScheduler scheduler = (JobScheduler) getSystemService(JOB_SCHEDULER_SERVICE);
                        int res = scheduler.schedule(info);
                        if(res == JobScheduler.RESULT_SUCCESS){
                            Log.d(TAG,"Job scheduled");
                        }else {
                            Log.d(TAG,"Job scheduled failed");
                        }

                    }else {
                        Toast.makeText(AddHwActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                        Title.setHintTextColor(Color.GRAY);
                        Class.setHintTextColor(Color.GRAY);
                        ExpireDate.setHintTextColor(Color.GRAY);
                        DateToNotify.setHintTextColor(Color.GRAY);
                    }
                }
            });

        }else {
            Toast.makeText(this, "Fields can't be empty", Toast.LENGTH_SHORT).show();
            Title.setHintTextColor(Color.RED);
            Class.setHintTextColor(Color.RED);
            ExpireDate.setHintTextColor(Color.RED);
            DateToNotify.setHintTextColor(Color.RED);
        }
    }
    public void StartAlarm(Date date){
        Log.d(TAG,"Alarm started");
        long timeinms = date.getTime();
        AlarmManager alarm = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(this,AlertReceiver.class);
        PendingIntent pintent = PendingIntent.getBroadcast(this,1,intent,0);
        if(date.before(new Date())){
            Toast.makeText(this,"Notification will not occur: Date is in the past",Toast.LENGTH_SHORT).show();
        }else {
            alarm.setExact(AlarmManager.RTC_WAKEUP,timeinms,pintent);
        }
    }





}

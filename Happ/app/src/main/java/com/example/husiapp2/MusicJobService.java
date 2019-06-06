package com.example.husiapp2;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.job.JobParameters;
import android.app.job.JobService;
import android.media.MediaPlayer;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.text.format.DateUtils;
import android.util.Log;

import java.util.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class MusicJobService extends JobService {
    public String DateToCompare;
    AddHwActivity hwa;
    public MusicJobService(){

    }
    private static final String TAG = "MusicJobService";
    private static final String Channel_ID = "com.example.husiapp2.ANDROID";
    private boolean jobCancelled = false;
    private boolean isWorking = false;
    static String Date;
    @Override
    public boolean onStartJob(JobParameters jobParameters) {
        Log.d(TAG,  "Music Job started");
        DoWork(jobParameters);
        isWorking = false;
        return isWorking;
    }

    @Override
    public boolean onStopJob(JobParameters jobParameters) {
        //IF user is not connected to wifi, stop job.
        Log.d(TAG, "Job cancelled");
        jobCancelled = true;
        boolean Reschedule = isWorking;
        return Reschedule;
    }
    public static boolean CompareDates(String date){
        Date = date;
        Boolean IsValid = false;
        Integer IsDateComparable = 0;
        String currentTime = new SimpleDateFormat("MM/dd/yy hh:mm").format(Calendar.getInstance().getTime());
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yy hh:mm");
        try {
            IsDateComparable += 1;
            Date DateToNotify = sdf.parse(Date);
            long DateToNotifyInMS = DateToNotify.getTime();
            Date CurrDate = sdf.parse(currentTime);
            long CurrDateInMS = CurrDate.getTime();
            Date DateToCompare = new Date(CurrDateInMS + (15 * 60000));
            long DateToCompareInMS = DateToCompare.getTime();
            Log.d(TAG,"DTN:"+DateToNotifyInMS+"CD:"+CurrDateInMS+"DTC:"+DateToCompareInMS);
            if((DateToNotifyInMS - CurrDateInMS) < 0){
                IsValid = true;
            }else {
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
                        if(CompareDates(date1)){
                            Log.d(TAG,"Notification starting");
                            CreateNotificationChannel();
                            StartNotification();
                            jobCancelled = true;
                            jobFinished(param, false);
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
    public void StartNotification(){
        Log.d(TAG,"Starting");
        final MediaPlayer mp = MediaPlayer.create(this,R.raw.plucky);
        mp.start();
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this,Channel_ID)
                .setSmallIcon(R.drawable.notificationicon)
                .setContentTitle("HUSIAPP:New Notification")
                .setContentText("Homework done?")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);
        NotificationManagerCompat nmc = NotificationManagerCompat.from(this);
        nmc.notify(1,builder.build());
    }
    public void CreateNotificationChannel(){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            CharSequence name = Channel_ID;
            String description = "default";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(Channel_ID,name,importance);
            channel.setDescription(description);
            NotificationManager nfmanager = getSystemService(NotificationManager.class);
            nfmanager.createNotificationChannel(channel);
        }
    }
}

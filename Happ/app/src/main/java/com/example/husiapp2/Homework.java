package com.example.husiapp2;

import android.view.View;

import java.time.LocalDate;
import java.util.Date;
import java.util.Locale;

public class Homework {
    public long Id;
    public String Title;
    public String Class;
    public String ExpireDate;
    public String DateToNotify;

    public Homework(){}

    public Homework(String title, String aClass,String expireDate,String dateToNotify) {
        Title = title;
        Class = aClass;
        ExpireDate = expireDate;
        DateToNotify = dateToNotify;
    }

    @Override
    public String toString(){
        return "Title:\n"+Title+"\nClass:\n"+Class+"\nExpire Date:\n"+ ExpireDate+"\nDate to notify:\n"+DateToNotify;
    }

}

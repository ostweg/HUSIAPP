package com.example.husiapp2;

public class User {
    public User(){

    }
    public String Firstname;
    public String Lastname;
    public String Password;
    public String Email;

    public User(String firstname, String lastname, String password, String email) {
        Firstname = firstname;
        Lastname = lastname;
        Password = password;
        Email = email;
    }
}

package com.example.user.employeedetails;

import android.net.Uri;
/**
 * Created by user on 7/20/2016.
 */
public class Employee {

    private int _id;
    private String _name, _phone, _email, _dob, _address;
    private Uri _imageURI;

    public Employee(int id, String name, String email, String phone, String dob, String address,Uri imageURI)
    {
        _id = id;
        _name = name;
        _email = email;
        _phone = phone;
        _dob = dob;
        _address = address;
        _imageURI = imageURI;
    }

    public int getId()
    {
        return _id;
    }

    public String getName()
    {
        return _name;
    }

    public String getEmail()
    {
        return _email;
    }

    public String getPhone()
    {
        return _phone;
    }

    public String getDob()
    {
        return _dob;
    }

    public String getAddress()
    {
        return _address;
    }

    public Uri getImageURI()
    {
        return _imageURI;
    }
}

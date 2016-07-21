package com.example.user.employeedetails;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.hardware.camera2.params.StreamConfigurationMap;
import android.net.Uri;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by user on 7/20/2016.
 */
public class DatabaseHandler extends SQLiteOpenHelper{

    private static final int DATABASE_VERSION = 1;

    private static final String DATABASE_NAME = "employeeManager",
    TABLE_EMPLOYEES = "employeeDetails",
    KEY_ID = "id",
    KEY_NAME = "name",
    KEY_EMAIL = "email",
    KEY_PHONE = "phone",
    KEY_DOB = "dob",
    KEY_ADDRESS = "address",
    KEY_IMAGEURI = "imageUri";

    public DatabaseHandler(Context context)
    {
        super(context,DATABASE_NAME,null,DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db){
        db.execSQL("CREATE TABLE " + TABLE_EMPLOYEES + "("+KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + KEY_NAME + " TEXT," + KEY_EMAIL + " TEXT," + KEY_PHONE + " TEXT," + KEY_DOB + " TEXT," + KEY_ADDRESS + " TEXT," + KEY_IMAGEURI + " TEXT )");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_EMPLOYEES);

        onCreate(db);
    }

    public void createEmployee(Employee employee)
    {
        SQLiteDatabase db = getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(KEY_NAME, employee.getName());
        values.put(KEY_EMAIL,employee.getEmail());
        values.put(KEY_PHONE,employee.getPhone());
        values.put(KEY_DOB,employee.getDob());
        values.put(KEY_ADDRESS,employee.getAddress());
        values.put(KEY_IMAGEURI,employee.getImageURI().toString());
        db.insert(TABLE_EMPLOYEES,null,values);
        db.close();
    }

    public Employee getEmployee( int id){
        SQLiteDatabase db = getReadableDatabase();

        Cursor cursor = db.query(TABLE_EMPLOYEES,new String[] { KEY_ID,KEY_NAME,KEY_EMAIL,KEY_PHONE,KEY_DOB,KEY_ADDRESS,KEY_IMAGEURI}, KEY_ID + "=?",new String[] { String.valueOf(id)},null,null,null,null);

        if(cursor != null)
        {
            cursor.moveToFirst();

        }
        Employee employee = new Employee(Integer.parseInt(cursor.getString(0)),cursor.getString(1),cursor.getString(2),cursor.getString(3),cursor.getString(4),cursor.getString(5), Uri.parse(cursor.getString(6)));
        db.close();
        cursor.close();
        return employee;
    }

    public void deleteEmployee(Employee employee)
    {
        SQLiteDatabase db = getWritableDatabase();
        db.delete(TABLE_EMPLOYEES,KEY_ID + "=?",new String[]{String.valueOf(employee.getId())});
        db.close();
    }

    public int getEmployeesCount() {
        //SELECT * FROM EMPLOYEES
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM "+TABLE_EMPLOYEES,null);
        int count = cursor.getCount();
        db.close();
        cursor.close();
        return count;
    }

    public int updateEmployees(Employee employee)
    {
        SQLiteDatabase db = getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_NAME,employee.getName());
        values.put(KEY_EMAIL,employee.getEmail());
        values.put(KEY_PHONE,employee.getPhone());
        values.put(KEY_DOB,employee.getDob());
        values.put(KEY_ADDRESS,employee.getAddress());
        values.put(KEY_IMAGEURI,employee.getImageURI().toString());

        int rowsAffected = db.update(TABLE_EMPLOYEES,values,KEY_ID + "=?",new String[] {String.valueOf(employee.getId())});
        db.close();
        return rowsAffected;
    }

    public List<Employee> getAllEmployees() {
        List<Employee> employees = new ArrayList<Employee>();

        SQLiteDatabase db = getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM "+TABLE_EMPLOYEES,null);

        if(cursor.moveToFirst()) {


            do {
                employees.add(new Employee(Integer.parseInt(cursor.getString(0)),cursor.getString(1),cursor.getString(2),cursor.getString(3),cursor.getString(4),cursor.getString(5),Uri.parse(cursor.getString(6))));
            }
            while(cursor.moveToNext());
        }
        cursor.close();
        db.close();
        Log.w("employees",employees.toString());
        return employees;
    }
}

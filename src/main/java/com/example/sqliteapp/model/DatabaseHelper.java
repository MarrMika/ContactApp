package com.example.sqliteapp.model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


import java.util.ArrayList;

public class DatabaseHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "Contact.db";
    public static final String TABLE_NAME = "contacts_table";
    public static final String COL_1 = "ID";
    public static final String COL_2 = "NAME";
    public static final String COL_3 = "PHONE";
    public static final String COL_4 = "BIRTHDAY";
    public static final String COL_5 = "DESCRIPTION";
    public static final String COL_6 = "PHOTO";


    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + TABLE_NAME +" (ID INTEGER PRIMARY KEY AUTOINCREMENT,NAME TEXT,PHONE TEXT,BIRTHDAY INTEGER,DESCRIPTION TEXT,PHOTO BLOB NOT NULL)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "+TABLE_NAME);
        onCreate(db);
    }

    public boolean insertData(String name,String phone,String birthday,String description,byte []photo) {
            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues contentValues = new ContentValues();
            contentValues.put(COL_2,name);
            contentValues.put(COL_3,phone);
            contentValues.put(COL_4,birthday);
            contentValues.put(COL_5,description);
            contentValues.put(COL_6,photo);
            long result = db.insert(TABLE_NAME,null ,contentValues);
                    if(result == -1)
                        return false;
                    else
                        return true;

    }

    public Cursor getAllData() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("select * from "+TABLE_NAME,null);
        return res;
    }

    public boolean updateData(String id,String name,String phone,String birthday,String description,byte []photo) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_2,name);
        contentValues.put(COL_3,phone);
        contentValues.put(COL_4,birthday);
        contentValues.put(COL_5,description);
        contentValues.put(COL_6,photo);
        db.update(TABLE_NAME, contentValues, "ID = ?",new String[] {id});
        return true;
    }

    public Integer deleteData (String id) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TABLE_NAME, "ID = ?",new String[] {id});
    }

    public Cursor getDataСursor(String sql){
        SQLiteDatabase database = getReadableDatabase();
        return database.rawQuery(sql, null);
    }

    public ArrayList<Contact> getData(){
        ArrayList<Contact> list = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("select * from "+TABLE_NAME,null);


        while(cursor.moveToNext()){
            int id = cursor.getInt(0);
            String name  = cursor.getString(1);
            String phone = cursor.getString(2);
            String birthday  = cursor.getString(3);
            String description = cursor.getString(4);
            byte[]image = cursor.getBlob(5);
            Contact contact = new Contact(id,image,name,phone,birthday,description);
            list.add(contact);
        }

        return list;
    }
}
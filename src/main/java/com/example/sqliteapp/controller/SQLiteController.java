package com.example.sqliteapp.controller;

import android.app.AlertDialog;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.sqliteapp.model.DatabaseHelper;
import com.example.sqliteapp.R;

import java.io.ByteArrayOutputStream;

public class SQLiteController {
    public void DeleteData(Button btnDelete, final DatabaseHelper myDb, final EditText editName, final Context context) {
        btnDelete.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Integer deletedRows = myDb.deleteData(editName.getText().toString());
                        if(deletedRows > 0)
                            Toast.makeText(context,"Data Deleted",Toast.LENGTH_LONG).show();
                        else
                            Toast.makeText(context,"Data not Deleted",Toast.LENGTH_LONG).show();
                    }
                }
        );
    }

    public void UpdateData(Button btnviewUpdate, final DatabaseHelper myDb,final EditText editId, final EditText editName,
                           final EditText editPhone, final EditText editBirthday, final EditText editDescription,
                           final ImageView photo,final Context context) {
        btnviewUpdate.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        boolean isUpdate = myDb.updateData(
                                editId.getText().toString(),
                                editName.getText().toString(),
                                editPhone.getText().toString(),
                                editBirthday.getText().toString(),
                                editDescription.getText().toString(),
                                imageViewToByte(photo));
                        if(isUpdate == true) {
                            Toast.makeText(context,"Data Update",Toast.LENGTH_LONG).show();
                        } else
                            Toast.makeText(context,"Data not Updated",Toast.LENGTH_LONG).show();
                    }
                }
        );
    }


    public byte[] imageViewToByte(ImageView image) {
        Bitmap bitmap = ((BitmapDrawable)image.getDrawable()).getBitmap();
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        byte[] byteArray = stream.toByteArray();
        return byteArray;
    }

    public  void AddData(Button btnAddData,final DatabaseHelper myDb,final EditText editName, final EditText editPhone,
                         final EditText editBirthday, final EditText editDescription,final ImageView photo, final Context context) {
        btnAddData.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        try {


                                boolean isInserted = myDb.insertData(editName.getText().toString(),
                                    editPhone.getText().toString(),
                                    editBirthday.getText().toString(),
                                    editDescription.getText().toString(),
                                    imageViewToByte(photo));
                                if (isInserted == true) {
                                    Toast.makeText(context, "Data Inserted", Toast.LENGTH_LONG).show();
                                    editName.setText("");
                                    editPhone.setText("");
                                    editBirthday.setText("");
                                    editDescription.setText("");
                                    photo.setImageResource(R.mipmap.ic_launcher);
                                } else
                                    Toast.makeText(context, "Data not Inserted", Toast.LENGTH_LONG).show();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
        );

    }

    public void viewAll(Button btnviewAll, final DatabaseHelper myDb, final Context context) {
        btnviewAll.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Cursor res = myDb.getAllData();
                        if(res.getCount() == 0) {
                            // show message
                            showMessage("Error","Nothing found",context);
                            return;
                        }

                        StringBuffer buffer = new StringBuffer();
                        while (res.moveToNext()) {
                            buffer.append("Id :"+ res.getString(0)+"\n");
                            buffer.append("Name :"+ res.getString(1)+"\n");
                            buffer.append("Phone :"+ res.getString(2)+"\n");
                            buffer.append("Birthday :"+ res.getString(3)+"\n");
                            buffer.append("Description :"+ res.getString(4)+"\n\n");
                        }

                        // Show all data
                        showMessage("Contacts",buffer.toString(),context);
                    }
                }
        );
    }

    public void showMessage(String title,String Message,Context context){
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setCancelable(true);
        builder.setTitle(title);
        builder.setMessage(Message);
        builder.show();
    }
}

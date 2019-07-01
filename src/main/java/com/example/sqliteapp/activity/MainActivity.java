package com.example.sqliteapp.activity;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.sqliteapp.R;
import com.example.sqliteapp.adapter.ContactAdapter;
import com.example.sqliteapp.controller.MessageController;
import com.example.sqliteapp.controller.SQLiteController;
import com.example.sqliteapp.model.Contact;
import com.example.sqliteapp.model.DatabaseHelper;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity  {
    private DatabaseHelper db;
    private SQLiteController sqLiteController;
    private Contact contactToCall;
    private ContactAdapter contactAdapter;
    private MessageController messageController;

    private ListView listView;
    public List<Contact> list;

    private static final int REQUEST_CALL = 1;
    final private int REQUEST_CODE_GALLERY = 101;

    private EditText name, phone, birthday,description;
    private ImageView btnUpdate,goBack,imageViewContact,addContact;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_main);
            init();
            updateListView();
            chooseItemToCall();
            setListeners();

    }

    private void setListeners(){




        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {

                CharSequence[] items = {"Edit", "Delete","Call","Send Message","Congratulate","Send encrypted message"};
                AlertDialog.Builder dialog = new AlertDialog.Builder(MainActivity.this);

                dialog.setTitle("Choose an action:");
                dialog.setItems(items, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int item) {
                        Contact contact = db.getData().get(position);
                        switch (item){
                            case 0:{
                                //edit                                              +
                                showDialogUpdate(MainActivity.this,contact);
                                break;
                            }
                            case 1:{
                                //delete                                            +
                                showDialogDelete(contact.getId());
                                updateListView();
                                break;
                            }
                            case 2:{
                                //Call                                              +
                                contactToCall=contact;
                                makePhoneCall();
                                break;

                            }
                            case 3:{
                                //Send message                                      +
                                startActivity(new Intent(Intent.ACTION_VIEW, Uri.fromParts("sms", contact.getPhone(), null)));
                                break;
                            }
                            case 4:{
                                //Congratulate                      +
                                Uri uri = Uri.parse("http://etnosoft.com.ua/wp-content/uploads/2019/01/11-3.jpg");
                                Intent shareIntent = new Intent();
                                shareIntent.setAction(Intent.ACTION_SEND);
                                shareIntent.putExtra(Intent.EXTRA_STREAM, uri);
                                shareIntent.setType("image/jpeg");
                                startActivity(Intent.createChooser(shareIntent, "Wish to:"+contact.getPhone()));
                                break;
                            }
                            case 5:{
                                //Send encrypted message
                                messageController.showDialogSentMessage(MainActivity.this,contact.getPhone(),getApplicationContext());
                                break;
                            }
                        }

                    }
                });
                dialog.show();
                return true;
            }
        });

        addContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder dialog = new AlertDialog.Builder(MainActivity.this);
                showDialogAdd(MainActivity.this);
                dialog.show();

            }
        });


    }

    private void showDialogAdd(Activity activity){

        final Dialog dialog = new Dialog(activity);
        dialog.setContentView(R.layout.activity_second);
        dialog.setTitle("Add new:");

        initEditField(dialog);



        int width = (int) (activity.getResources().getDisplayMetrics().widthPixels * 1.0);
        int height = (int) (activity.getResources().getDisplayMetrics().heightPixels * 0.90);
        dialog.getWindow().setLayout(width, height);
        dialog.show();

        imageViewContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // request photo library
                ActivityCompat.requestPermissions(
                        MainActivity.this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                        REQUEST_CODE_GALLERY
                );
            }
        });

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    db.insertData(
                            name.getText().toString().trim(),
                            phone.getText().toString().trim(),
                            birthday.getText().toString().trim(),
                            description.getText().toString().trim(),
                            imageViewToByte(imageViewContact)
                    );
                    dialog.dismiss();
                    Toast.makeText(getApplicationContext(), "Add successfully!!!",Toast.LENGTH_SHORT).show();
                }
                catch (Exception error) {
                    Log.e("Add error", error.getMessage());
                }
                updateListView();
            }
        });


        goBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               dialog.dismiss();
            }
        });
    }

    private void initEditField(Dialog dialog){
        imageViewContact = (ImageView) dialog.findViewById(R.id.imageViewContact);
        name = (EditText) dialog.findViewById(R.id.editText_name);
        phone = (EditText) dialog.findViewById(R.id.editText_phone);
        birthday = (EditText) dialog.findViewById(R.id.editText_birthday);
        description = (EditText) dialog.findViewById(R.id.editText_description);
        btnUpdate = (ImageView) dialog.findViewById(R.id.button_update);
        goBack = (ImageView)dialog.findViewById(R.id.btnGoBack);
    }

    private void showDialogUpdate(Activity activity, final Contact contact){
        final int id = contact.getId();

        final Dialog dialog = new Dialog(activity);
        dialog.setContentView(R.layout.activity_second);
        dialog.setTitle("Update");

        initEditField(dialog);

        name.setText(contact.getName());
        phone.setText(contact.getPhone());
        birthday.setText(contact.getBirthday());
        description.setText(contact.getDescription());

        byte[] foodImage = contact.getImageContact();
        Bitmap image = BitmapFactory.decodeByteArray(foodImage, 0, foodImage.length);
        imageViewContact.setImageBitmap(image);


        int width = (int) (activity.getResources().getDisplayMetrics().widthPixels * 1.0);
        int height = (int) (activity.getResources().getDisplayMetrics().heightPixels * 1.0);
        dialog.getWindow().setLayout(width, height);
        dialog.show();

        imageViewContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // request photo library
                ActivityCompat.requestPermissions(
                        MainActivity.this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                        REQUEST_CODE_GALLERY
                );
            }
        });

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    db.updateData(
                            ""+id,
                            name.getText().toString().trim(),
                            phone.getText().toString().trim(),
                            birthday.getText().toString().trim(),
                            description.getText().toString().trim(),
                            imageViewToByte(imageViewContact)
                    );
                    dialog.dismiss();
                    Toast.makeText(getApplicationContext(), "Edit successfully!!!",Toast.LENGTH_SHORT).show();
                }
                catch (Exception error) {
                    Log.e("Update error", error.getMessage());
                }
                     updateListView();
            }
        });
        goBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                  dialog.dismiss();
            }
        });


    }

    private void showDialogDelete(final int id){
        final AlertDialog.Builder dialogDelete = new AlertDialog.Builder(MainActivity.this);

        dialogDelete.setTitle("Warning!!");
        dialogDelete.setMessage("Are you sure you want to this delete?");
        dialogDelete.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                try {
                    db.deleteData(""+id);
                    Toast.makeText(getApplicationContext(), "Delete successfully!!!",Toast.LENGTH_SHORT).show();
                } catch (Exception e){
                    Log.e("error", e.getMessage());
                }
                updateListView();
            }
        });

        dialogDelete.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        dialogDelete.show();
    }

    public byte[] imageViewToByte(ImageView image) {
        Bitmap bitmap = ((BitmapDrawable)image.getDrawable()).getBitmap();
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        byte[] byteArray = stream.toByteArray();
        return byteArray;
    }

    private void chooseItemToCall(){
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                contactToCall = list.get(position);
                //new SecondActivity().showMessage("ListViewItem","Item is choosen");
                makePhoneCall();
            }
        });
    }

    private void makePhoneCall(){
        String phone = contactToCall.getPhone();
        if(phone.trim().length()>0) {

            if (ContextCompat.checkSelfPermission(MainActivity.this,
                    Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(MainActivity.this,
                        new String[]{Manifest.permission.CALL_PHONE},REQUEST_CALL);
            } else {
                String dial = "tel:" + phone;
                startActivity(new Intent(Intent.ACTION_CALL, Uri.parse(dial)));
            }
        }else{
            sqLiteController.showMessage("Error","Invalid number",this);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        if(requestCode == REQUEST_CODE_GALLERY){
            if(grantResults.length >0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent, REQUEST_CODE_GALLERY);
            }
            else {
                Toast.makeText(getApplicationContext(), "You don't have permission to access file location!", Toast.LENGTH_SHORT).show();
            }
            return;
        }

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if(requestCode == REQUEST_CODE_GALLERY && resultCode == RESULT_OK && data != null){
            Uri uri = data.getData();

            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), uri);
                Toast.makeText(MainActivity.this, "Image Saved!", Toast.LENGTH_SHORT).show();
                imageViewContact.setImageBitmap(bitmap);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    public void updateListView(){
        list = db.getData();
        contactAdapter = new ContactAdapter(this,list);
        listView.setAdapter(contactAdapter);
        contactAdapter.notifyDataSetChanged();
    }

    private  void init(){
        db = new DatabaseHelper(this);
        sqLiteController = new SQLiteController();
        messageController = new MessageController();

        addContact = (ImageView)findViewById(R.id.btnAddContact) ;
        listView = (ListView)findViewById(R.id.listViewContacts);
        list = new ArrayList<>();

    }


}
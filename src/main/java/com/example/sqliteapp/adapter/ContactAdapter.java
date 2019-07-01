package com.example.sqliteapp.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import com.example.sqliteapp.R;
import com.example.sqliteapp.controller.HolderController;
import com.example.sqliteapp.model.Contact;

import java.util.List;

public class ContactAdapter extends BaseAdapter {
    Context context;
    List<Contact> contactList;

    public ContactAdapter(Context context,List<Contact> contactList) {
        this.context = context;
        this.contactList = contactList;
    }

    @Override
    public int getCount() {
        return this.contactList.size();
    }

    @Override
    public Object getItem(int position) {
        return contactList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }



    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        HolderController holderController;

           if(convertView==null) {
               LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
               convertView = inflater.inflate(R.layout.contact_list_item, null);
               holderController = new HolderController(convertView);
               convertView.setTag(holderController);
           }else{
               holderController = (HolderController)convertView.getTag();
           }

           Contact contact = contactList.get(position);

           holderController.name.setText(contact.getName());
           holderController.phone.setText(contact.getPhone());

           byte[] imageContact = contact.getImageContact();
           Bitmap bitmap1 = BitmapFactory.decodeByteArray(imageContact, 0, imageContact.length);
           holderController.imageContact.setImageBitmap(bitmap1);


           if(contact.checkBirthdayPerson()) {
               holderController.imageCall_Present.setImageResource(R.drawable.red_present);
           }


        return convertView;
    }




}

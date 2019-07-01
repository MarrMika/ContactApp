package com.example.sqliteapp.controller;


import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.sqliteapp.R;

public class HolderController {


    public ImageView imageContact, imageCall_Present;
    public TextView name, phone;

    public HolderController(View v) {
        imageContact = (ImageView) v.findViewById(R.id.imageViewContactItem);
        name = (TextView) v.findViewById(R.id.textViewName);
        phone = (TextView) v.findViewById(R.id.textViewPhone);
        imageCall_Present = (ImageView) v.findViewById(R.id.imageViewCall_Present);


    }

}



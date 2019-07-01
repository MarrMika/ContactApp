package com.example.sqliteapp.controller;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.sqliteapp.R;

public class MessageController {
    final int SENT_SMS_PERMISSION=1;
    EncryptedMessageController encryptedMessageController;
    EditText message,messageToEncode, messageDecode, textKey ;

    ImageView btnSend ,btnGoBack;
    Button btnEncode,btnDecode ;


    public MessageController() {
        this.encryptedMessageController  = new EncryptedMessageController();
    }

    public void showDialogSentMessage(final Activity activity, final String phone, final Context context){

        final String[] phase = new String[1];

        final Dialog dialog = new Dialog(activity);
        dialog.setContentView(R.layout.activity_message);
        dialog.setTitle("Add new:");


        message = (EditText) dialog.findViewById(R.id.editTextMessage);
        messageToEncode = (EditText) dialog.findViewById(R.id.editTextToEncode);
        messageDecode = (EditText) dialog.findViewById(R.id.editTextDecode);

        textKey = (EditText) dialog.findViewById(R.id.editTextKey);

        btnSend = (ImageView) dialog.findViewById(R.id.btnSend);
        btnGoBack = (ImageView)dialog.findViewById(R.id.btnGoBack);
        btnEncode = (Button) dialog.findViewById(R.id.btnEncode);
        btnDecode = (Button) dialog.findViewById(R.id.btnDecode);

        btnEncode.setEnabled(false);
        btnSend.setEnabled(false);

        if(textKey.getText()!=null&&messageToEncode.getText()!=null) btnEncode.setEnabled(true);
        if(checkPermission(Manifest.permission.SEND_SMS,context)){
            btnSend.setEnabled(true);
        }else{
            ActivityCompat.requestPermissions(activity,
                    new String[]{Manifest.permission.SEND_SMS},SENT_SMS_PERMISSION);
            btnSend.setEnabled(true);
        }

        int width = (int) (activity.getResources().getDisplayMetrics().widthPixels * 1.0);
        int height = (int) (activity.getResources().getDisplayMetrics().heightPixels * 1.0);
        dialog.getWindow().setLayout(width, height);
        dialog.show();

        btnEncode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String key = textKey.getText().toString().trim();
                int lengthOfPhase = encryptedMessageController.getCodeLength(2,key);
                phase[0] = encryptedMessageController.getPhase(lengthOfPhase,key);

                byte[] sms = encryptedMessageController.encode(messageToEncode.getText().toString().trim(), phase[0]);
                message.setText(new String(sms));
            }
        });

        btnDecode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String  sms = encryptedMessageController.decode(message.getText().toString().trim().getBytes(),phase[0]);
                messageDecode.setText(sms);
            }
        });

        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onSend(phone,message.getText().toString().trim(),context);
                dialog.dismiss();

            }
        });

        btnGoBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }

    public void onSend(String phoneNumber,String smsMessage,Context context){
        if(phoneNumber==null||phoneNumber.length()==0||
                smsMessage==null||smsMessage.length()==0){
            return;
        }

        if(checkPermission(Manifest.permission.SEND_SMS,context)){
            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage(phoneNumber,null,smsMessage,null,null);
            Toast.makeText(context,"Message was sent!",Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(context,"Permission was denied!",Toast.LENGTH_SHORT).show();
        }
    }

    boolean checkPermission(String permission,Context context){
        int check = ContextCompat.checkSelfPermission(context.getApplicationContext(),permission);
        return (check== PackageManager.PERMISSION_GRANTED);
    }
}

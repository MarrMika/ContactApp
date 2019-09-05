package com.example.sqliteapp.model;

import android.annotation.TargetApi;
import android.icu.util.Calendar;
import android.os.Build;

public class Contact {
    private int id;
    private byte[] imageContact;
    private String name;
    private String phone;
    private String birthday;
    private String description;

    public Contact(int id,byte[] imageContact, String name, String phone,String birthday,String description) {
        this.id=id;
        this.imageContact = imageContact;
        this.name = name;
        this.phone = phone;
        this.birthday = birthday;
        this.description = description;
    }

    public int getId() {
        return id;
    }

    public byte[] getImageContact() {
        return imageContact;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public String getBirthday() {
        return birthday;
    }

    public String getDescription() {
        return description;
    }


    @TargetApi(Build.VERSION_CODES.N)
    public boolean checkBirthdayPerson(){
        boolean birthdayPermission = false;
        Calendar calendar= Calendar.getInstance();
        int int_month = calendar.get(Calendar.MONTH)+1;
        String monthBirthday = "0"+int_month;
        String dayBirthday = ""+calendar.get(Calendar.DAY_OF_MONTH);
        String birthday = getBirthday().trim();
        if (birthday.length()!=0&&birthday.length()>5) {
            String day = birthday.substring(0, 2);
            String month = birthday.substring(3, 5);

            birthdayPermission = monthBirthday.equals(month) &&
                    dayBirthday.equals(day);

        }
        return birthdayPermission;
    }

}

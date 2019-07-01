package com.example.sqliteapp.controller;

import static java.lang.Math.pow;

public class EncryptedMessageController {
    int []arrayPhase;


    public byte[] encode(String secret, String key){
        byte[] btxt = null;
        byte[] bkey = null;

        btxt = secret.getBytes();
        bkey = key.getBytes();
        byte[] result = new byte[secret.length()];

        for (int i = 0; i < btxt.length; i++) {
            result[i] = (byte) (btxt[i] ^ bkey[i % bkey.length]);
        }
        return result;
    }

    public String decode(byte[] secret, String key) {
        byte[] result = new byte[secret.length];
        byte[] bkey = key.getBytes();

        for (int i = 0; i < secret.length; i++) {
            result[i] = (byte) (secret[i] ^ bkey[i % bkey.length]);
        }
        return new String(result);
    }

    public String getPhase(int n,String  key1){
        String result = "";
        int phase = /*Integer.parseInt("111"); */Integer.parseInt(makePhase(key1.length()).trim());
        int key = Integer.parseInt(key1);


        //  int mask = 111;
        while(n>0) {
            int res = key & phase;
            int count = Integer.bitCount(res) % 2;

            if (count == 0){
                result += "0";
                phase = phase<<1;
            }else {
                result += "1";
                phase = phase<<1;
                phase = phase+1;

            }

            n--;
        }

        return result;
    }

    public String makePhase(int n){
        String mainPhase = " ";
        int numberOfBits = n;

        arrayPhase = new int[numberOfBits];
        int count = fillPhase(n);
        while(count == n){
            count=0;
            count = fillPhase(n);
        }

        for(int i =0;i<n;i++)
            mainPhase+=""+arrayPhase[i];
        return mainPhase;
    }

    public int fillPhase(int n){
        int count = 0;
        for (int i = 0; i < n; i++) {
            arrayPhase[i] = (int) (Math.random() * 2);
            if (arrayPhase[i] == 0) {
                count++;

            }else if(arrayPhase[0]==0){
                    if(count<3)
                        count++;
            }

        }
        return count;
    }



    int getCodeLength(int maxVal, String key) {
        return (int) pow(maxVal, key.length()) - 1;
    }
}

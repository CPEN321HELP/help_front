package com.example.help_m5;

import android.content.Context;
import android.graphics.Color;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

public final class MemoryData {

    public static void saveData(String data, Context context) {
        try {
            FileOutputStream fileOutputStream = context.openFileOutput("datata.txt", Context.MODE_PRIVATE);
            OutputStreamWriter osw = new OutputStreamWriter(fileOutputStream);
            osw.write(data);
            osw.flush();
            osw.close();
            //fileOutputStream.write(data.getBytes());
            //fileOutputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void saveName(String data, Context context) {
        try {
            FileOutputStream fileOutputStream = context.openFileOutput("nameee.txt", Context.MODE_PRIVATE);
            OutputStreamWriter osw = new OutputStreamWriter(fileOutputStream);
            osw.write(data);
            osw.flush();
            osw.close();
            //fileOutputStream.write(data.getBytes());
            //fileOutputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void saveLastMsgTS(String data, String chatId, Context context) {
        try {
            FileOutputStream fileOutputStream = context.openFileOutput(chatId+".txt", Context.MODE_PRIVATE);
            OutputStreamWriter osw = new OutputStreamWriter(fileOutputStream);
            osw.write(data);
            osw.flush();
            osw.close();
            //fileOutputStream.write(data.getBytes());
            //fileOutputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String getData(Context context) {
        String data = "";
        try {
            FileInputStream fis = context.openFileInput("datata.txt");
            InputStreamReader isr = new InputStreamReader(fis);
            BufferedReader bufferedReader = new BufferedReader(isr);
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                sb.append(line);
            }
            data = sb.toString();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return data;
    }

    public static String getName(Context context) {
        String data = "";
        try {
            FileInputStream fis = context.openFileInput("nameee.txt");
            InputStreamReader isr = new InputStreamReader(fis);
            BufferedReader bufferedReader = new BufferedReader(isr);
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                sb.append(line);
            }
            data = sb.toString();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return data;
    }

    public static String getLastMsgTS(Context context, String chatId) {
        String data = "";
        try {
            FileInputStream fis = context.openFileInput(chatId+".txt");
            InputStreamReader isr = new InputStreamReader(fis);
            BufferedReader bufferedReader = new BufferedReader(isr);
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                sb.append(line);
            }
            data = sb.toString();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return data;
    }



}

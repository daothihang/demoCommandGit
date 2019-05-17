package com.example.sharefood.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class DateTime {

    public static String getCurrentDate(){
        return new SimpleDateFormat("yyyyMMdd_HHmmss").format(Calendar.getInstance().getTime());
    }
}

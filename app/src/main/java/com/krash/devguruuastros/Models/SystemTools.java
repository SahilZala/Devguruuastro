package com.krash.devguruuastros.Models;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class SystemTools {
    static String current_time,current_date;

    static public String getCurrent_time() {
        current_time = new SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(new Date());
        return current_time;
    }

    public static String getCurrent_date() {
        current_date = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());
        return current_date;
    }

}

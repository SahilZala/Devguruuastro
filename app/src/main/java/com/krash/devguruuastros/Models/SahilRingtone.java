package com.krash.devguruuastros.Models;

import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;

import java.net.URI;

public class SahilRingtone {
    static Ringtone ringtone = null;
    static Uri notification = null;

    public SahilRingtone() {

    }

    public SahilRingtone(Ringtone ringtone, Uri notification)
    {
        this.ringtone = ringtone;
        this.notification = notification;
    }


    public static Ringtone getRingtone() {
        return ringtone;
    }

    public static void setRingtone(Ringtone ringtone) {
        SahilRingtone.ringtone = ringtone;
    }

    public static Uri getNotification() {
        return notification;
    }

    public static void setNotification(Uri notification) {
        SahilRingtone.notification = notification;
    }


    public static void play()
    {
        ringtone.play();
    }

    public static void stop()
    {
        ringtone.stop();
    }

    public static boolean isPlaying()
    {
        if(ringtone.isPlaying())
        {
            return true;
        }
        else return false;
    }
}

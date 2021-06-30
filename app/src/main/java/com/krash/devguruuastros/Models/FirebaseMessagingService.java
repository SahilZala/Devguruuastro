package com.krash.devguruuastros.Models;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Vibrator;
import android.util.Log;
import android.widget.RemoteViews;

import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.app.NotificationCompat;

import com.google.firebase.messaging.RemoteMessage;
import com.krash.devguruuastros.Activities.SplashActivity;
import com.krash.devguruuastros.Activities.UserCallActivity;
import com.krash.devguruuastros.Activities.UsersActivity;
import com.krash.devguruuastros.R;
import com.sinch.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

public class FirebaseMessagingService extends com.google.firebase.messaging.FirebaseMessagingService {


//    private static final String TAG = "FirebaseMessagingServic";
//
//    @Override
//    public void onMessageReceived(RemoteMessage remoteMessage) {
//        super.onMessageReceived(remoteMessage);
//        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
//
//        if (remoteMessage.getData().size() > 0) {
//            Log.d(TAG, "Message data payload: " + remoteMessage.getData());
//            try {
//                JSONObject data = new JSONObject(remoteMessage.getData());
//                String jsonMessage = data.getString("extra_information");
//                Log.d(TAG, "onMessageReceived: \n" +
//                        "Extra Information: " + jsonMessage);
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
//        }
//
//        // Check if message contains a notification payload.
//        if (remoteMessage.getNotification() != null) {
//            String title = remoteMessage.getNotification().getTitle(); //get title
//            String message = remoteMessage.getNotification().getBody(); //get message
//            String click_action = remoteMessage.getNotification().getClickAction(); //get click_action
//
//            Log.d(TAG, "Message Notification Title: " + title);
//            Log.d(TAG, "Message Notification Body: " + message);
//            Log.d(TAG, "Message Notification click_action: " + click_action);
//
//            sendNotification(title, message,click_action);
//        }
//    }
//
//
//    @Override
//    public void onDeletedMessages() {
//
//    }
//
//    private void sendNotification(String title,String messageBody, String click_action) {
//        Intent intent;
//        if(click_action.equals("USERSACTIVITY")){
//            intent = new Intent(this, UsersActivity.class);
//            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//        }
//        else if(click_action.equals("MAINACTIVITY")){
//            intent = new Intent(this, SplashActivity.class);
//            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//        }else{
//            intent = new Intent(this, SplashActivity.class);
//            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//        }
//
//        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
//                PendingIntent.FLAG_ONE_SHOT);
//
//        Uri defaultSoundUri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
//        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
//                .setSmallIcon(R.mipmap.ic_launcher)
//                .setContentTitle(title)
//                .setContentText(messageBody)
//                .setAutoCancel(true)
//                .setSound(defaultSoundUri)
//                .setContentIntent(pendingIntent);
//
//        NotificationManager notificationManager =
//                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
//
//        notificationManager.notify(0 /* ID of notification */, notificationBuilder.build());
//    }


    NotificationManager mNotificationManager;


    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);


//        showNotification(remoteMessage.getNotification().getTitle(),remoteMessage.getNotification().getBody());


// playing audio and vibration when user se reques
       Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE);
       Ringtone r = RingtoneManager.getRingtone(getApplicationContext(), notification);
//        r.play();

        SahilRingtone sr = new SahilRingtone(r,notification);
        SahilRingtone.play();

//
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
//            r.setLooping(false);
//        }

//        // vibration
//        Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
//        long[] pattern = {100, 300, 300, 300};
//        v.vibrate(pattern, -1);


        //  int resourceImage = getResources().getIdentifier(remoteMessage.getNotification().getIcon(), "drawable", getPackageName());




        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "com.krash.devguruuastros.ANDROID");

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//            builder.setSmallIcon(R.drawable.icontrans);
            builder.setSmallIcon(R.drawable.roundlogo);
        } else {
//            builder.setSmallIcon(R.drawable.icon_kritikar);
            builder.setSmallIcon(R.drawable.roundlogo);
        }

        if(remoteMessage.getData().get("click_action").equals("USERSACTIVITY"))
        {
            Log.d("notichat","chat");
            Intent resultIntent = new Intent(this, UsersActivity.class);
            PendingIntent pendingIntent = PendingIntent.getActivity(this, 1, resultIntent, PendingIntent.FLAG_UPDATE_CURRENT);


            builder.setContentTitle(remoteMessage.getData().get("title"));
            builder.setContentText(remoteMessage.getData().get("body"));
            builder.setContentIntent(pendingIntent);
            builder.setStyle(new NotificationCompat.BigTextStyle().bigText(remoteMessage.getData().get("body")));
            builder.setAutoCancel(true);
            builder.setPriority(Notification.PRIORITY_MAX);
        }
        else if(remoteMessage.getData().get("click_action").equals("USERCALLACTIVITY")){

            Log.d("noticall","call");
            Intent resultIntent = new Intent(this, UserCallActivity.class);
            PendingIntent pendingIntent = PendingIntent.getActivity(this, 1, resultIntent, PendingIntent.FLAG_UPDATE_CURRENT);


            builder.setContentTitle(remoteMessage.getData().get("title"));
            builder.setContentText(remoteMessage.getData().get("body"));
            builder.setContentIntent(pendingIntent);
            builder.setStyle(new NotificationCompat.BigTextStyle().bigText(remoteMessage.getData().get("body")));
            builder.setAutoCancel(true);
            builder.setPriority(Notification.PRIORITY_MAX);
        }
        else
        {
            Intent resultIntent = new Intent(this, SplashActivity.class);
            PendingIntent pendingIntent = PendingIntent.getActivity(this, 1, resultIntent, PendingIntent.FLAG_UPDATE_CURRENT);

            builder.setContentTitle(remoteMessage.getData().get("title"));
            builder.setContentText(remoteMessage.getData().get("body"));
            builder.setContentIntent(pendingIntent);
            builder.setStyle(new NotificationCompat.BigTextStyle().bigText(remoteMessage.getData().get("body")));
            builder.setAutoCancel(true);
            builder.setPriority(Notification.PRIORITY_MAX);
        }




        mNotificationManager =
                (NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);




        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
        {
            String channelId = "com.krash.devguruuastros.ANDROID";
            NotificationChannel channel = new NotificationChannel(
                    channelId,
                    "Channel human readable title",
                    NotificationManager.IMPORTANCE_DEFAULT);
            mNotificationManager.createNotificationChannel(channel);

            builder.setChannelId(channelId);
        }



// notificationId is a unique int for each notification that you must define

        try {
            mNotificationManager.notify(100, builder.build());
        }
        catch(Exception ex)
        {

        }

    }

//
//    public void showNotification(String title,
//                                 String message) {
//        // Pass the intent to switch to the MainActivity
//        Intent intent
//                = new Intent(this, SplashActivity.class);
//        // Assign channel ID
//        String channel_id = "notification_channel";
//        // Here FLAG_ACTIVITY_CLEAR_TOP flag is set to clear
//        // the activities present in the activity stack,
//        // on the top of the Activity that is to be launched
//        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//        // Pass the intent to PendingIntent to start the
//        // next Activity
//        PendingIntent pendingIntent
//                = PendingIntent.getActivity(
//                this, 0, intent,
//                PendingIntent.FLAG_ONE_SHOT);
//
//        Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE);
//
//        AudioAttributes audioAttributes = new AudioAttributes.Builder()
//                .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
//                .setUsage(AudioAttributes.USAGE_NOTIFICATION_EVENT)
//                .build();
//
//        // Create a Builder object using NotificationCompat
//        // class. This will allow control over all the flags
//        NotificationCompat.Builder builder
//                = new NotificationCompat
//                .Builder(getApplicationContext(),
//                channel_id)
//                .setSmallIcon(R.drawable.loogo3)
//                .setAutoCancel(true)
//                .setVibrate(new long[]{1000, 1000, 1000,
//                        1000, 1000})
//                .setOnlyAlertOnce(true)
//                .setContentIntent(pendingIntent);
//
//
//
//        // A customized design for the notification can be
//        // set only for Android versions 4.1 and above. Thus
//        // condition for the same is checked here.
//        if (Build.VERSION.SDK_INT
//                >= Build.VERSION_CODES.JELLY_BEAN) {
//            builder = builder.setContent(
//                    getCustomDesign(title, message));
//        } // If Android Version is lower than Jelly Beans,
//        // customized layout cannot be used and thus the
//        // layout is set as follows
//        else {
//            builder = builder.setContentTitle(title)
//                    .setContentText(message)
//                    .setSmallIcon(R.drawable.loogo3);
//        }
//        // Create an object of NotificationManager class to
//        // notify the
//        // user of events that happen in the background.
//        NotificationManager notificationManager
//                = (NotificationManager) getSystemService(
//                Context.NOTIFICATION_SERVICE);
//        // Check if the Android Version is greater than Oreo
//        if (Build.VERSION.SDK_INT
//                >= Build.VERSION_CODES.O) {
//            NotificationChannel notificationChannel
//                    = new NotificationChannel(
//                    channel_id, "notification_channel",
//                    NotificationManager.IMPORTANCE_HIGH);
//
//
//            notificationChannel.setSound(notification,audioAttributes);
//            notificationManager.createNotificationChannel(
//                    notificationChannel);
//        }
//
//        notificationManager.notify(0, builder.build());
//    }
//
//    private RemoteViews getCustomDesign(String title,
//                                        String message) {
//        RemoteViews remoteViews = new RemoteViews(
//                getApplicationContext().getPackageName(),
//                R.layout.notification);
//        remoteViews.setTextViewText(R.id.title, title);
//        remoteViews.setTextViewText(R.id.message, message);
//        remoteViews.setImageViewResource(R.id.icon,
//                R.drawable.loogo3);
//        return remoteViews;
//    }

}



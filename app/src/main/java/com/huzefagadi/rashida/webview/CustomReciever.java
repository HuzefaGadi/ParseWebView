package com.huzefagadi.rashida.webview;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.parse.ParsePushBroadcastReceiver;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Iterator;

/**
 * Created by Rashida on 16/06/15.
 */
public class CustomReciever extends ParsePushBroadcastReceiver {
    private static final String TAG = "CustomReceiver";
    private NotificationManager mNotificationManager;
    private Context mContext;


    @Override
    protected void onPushReceive(Context context, Intent intent) {
        {
            mContext = context.getApplicationContext();
            try {
                if (intent == null) {
                    Log.d(TAG, "Receiver intent null");
                } else {
                    String action = intent.getAction();
                    Log.d(TAG, "got action " + action);

                    if (action.equals("com.parse.push.intent.RECEIVE")) {
                       // String channel = intent.getExtras().getString("com.parse.Channel");
                        JSONObject json = new JSONObject(intent.getExtras().getString("com.parse.Data"));
                        sendNotification(json.getString("message"), json.getString("link"), json.getString("title"));

                    }
                }
            } catch (JSONException e) {
                Log.d(TAG, "JSONException: " + e.getMessage());
            }
        }
    }


    private void sendNotification(String msg, String link, String title) {
        mNotificationManager = (NotificationManager)
                mContext.getSystemService(Context.NOTIFICATION_SERVICE);

        Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        Intent in = new Intent(mContext, MainActivity.class);
        in.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        in.putExtra("notification_id", 1);
        in.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        in.putExtra("link", link);
        PendingIntent contentIntent = PendingIntent.getActivity(mContext, 0, in, PendingIntent.FLAG_UPDATE_CURRENT);


        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(mContext)
                        .setSmallIcon(R.mipmap.ic_launcher)
                        .setContentTitle(title)
                        .setSound(alarmSound)
                        .setAutoCancel(true)
                        .setStyle(new NotificationCompat.BigTextStyle()
                                .bigText(msg))
                        .setContentText(msg);

        mBuilder.setContentIntent(contentIntent);
        mNotificationManager.notify(1, mBuilder.build());

    }
}
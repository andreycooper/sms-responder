package com.weezlabs.smsresponder;


import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.provider.Telephony;
import android.support.v4.app.NotificationCompat;
import android.telephony.SmsMessage;

public class SmsListener extends BroadcastReceiver {

    public static final String MESSAGE_KEY = "sms_message_key";
    private static final String TAG = SmsListener.class.getSimpleName();

    @SuppressLint("NewApi")
    @Override
    public void onReceive(Context context, Intent intent) {
        if (Telephony.Sms.Intents.SMS_RECEIVED_ACTION.equals(intent.getAction())) {
            abortBroadcast();
            StringBuilder stringBuilder = new StringBuilder();
            for (SmsMessage smsMessage : Telephony.Sms.Intents.getMessagesFromIntent(intent)) {
                stringBuilder.append("Message received from: ")
                        .append(smsMessage.getDisplayOriginatingAddress())
                        .append(" with text: ")
                        .append(smsMessage.getDisplayMessageBody());

                SmsIntentService.startActionReceiveSms(context,
                        smsMessage.getDisplayOriginatingAddress(),
                        smsMessage.getDisplayMessageBody());
            }
            showNotification(context, stringBuilder.toString());
        }
    }

    private void showNotification(Context context, String messageText) {
        Intent intent = new Intent(context, MainActivity.class);
        intent.putExtra(MESSAGE_KEY, messageText);
        PendingIntent contentIntent = PendingIntent.getActivity(context, 0,
                intent, PendingIntent.FLAG_CANCEL_CURRENT);


        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(context)
                        .setSmallIcon(R.mipmap.ic_launcher)
                        .setContentTitle("SMS notification")
                        .setContentText(messageText);
        mBuilder.setContentIntent(contentIntent);
        mBuilder.setDefaults(Notification.DEFAULT_SOUND);
        mBuilder.setAutoCancel(true);
        NotificationManager mNotificationManager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.notify(1, mBuilder.build());

    }
}
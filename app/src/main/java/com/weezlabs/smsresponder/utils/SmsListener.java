package com.weezlabs.smsresponder.utils;


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

import com.weezlabs.smsresponder.MainActivity;
import com.weezlabs.smsresponder.R;
import com.weezlabs.smsresponder.utils.MessageUtil;
import com.weezlabs.smsresponder.utils.SmsIntentService;

public class SmsListener extends BroadcastReceiver {

    public static final String MESSAGE_KEY = "com.weezlabs.smsresponder.action.SMS_MESSAGE";
    public static final int NOTIFY_ID = 12345;

    @SuppressLint("NewApi")
    @Override
    public void onReceive(Context context, Intent intent) {
        if (Telephony.Sms.Intents.SMS_RECEIVED_ACTION.equals(intent.getAction())) {
            StringBuilder stringBuilder = new StringBuilder();
            for (SmsMessage smsMessage : Telephony.Sms.Intents.getMessagesFromIntent(intent)) {
                if (MessageUtil.isStoredNumber(context, smsMessage.getDisplayOriginatingAddress())) {
                    abortBroadcast();
                    stringBuilder.append(context.getString(R.string.notification_phone_number_text))
                            .append(smsMessage.getDisplayOriginatingAddress())
                            .append(context.getString(R.string.notification_sms_text))
                            .append(smsMessage.getDisplayMessageBody());

                    SmsIntentService.startActionReceiveSms(context,
                            smsMessage.getDisplayOriginatingAddress(),
                            smsMessage.getDisplayMessageBody());
                }
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
                        .setContentTitle(context.getString(R.string.notification_title_text))
                        .setContentText(messageText);
        mBuilder.setContentIntent(contentIntent);
        mBuilder.setDefaults(Notification.DEFAULT_SOUND);
        mBuilder.setAutoCancel(true);
        NotificationManager mNotificationManager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.notify(NOTIFY_ID, mBuilder.build());

    }
}
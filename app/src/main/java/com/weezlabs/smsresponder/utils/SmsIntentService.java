package com.weezlabs.smsresponder.utils;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.telephony.SmsManager;
import android.util.Log;
import android.widget.Toast;

import com.weezlabs.smsresponder.R;

/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p/>
 */
public class SmsIntentService extends IntentService {
    private static final String ACTION_RECEIVE_SMS = "com.weezlabs.smsresponder.action.RECEIVE_SMS";

    private static final String EXTRA_PHONE_NUMBER = "com.weezlabs.smsresponder.extra.PHONE_NUMBER";
    private static final String EXTRA_SMS_BODY = "com.weezlabs.smsresponder.extra.SMS_BODY";
    private static final String LOG_TAG = SmsIntentService.class.getSimpleName();

    /**
     * Starts this service to perform action ReceiveSms with the given parameters. If
     * the service is already performing a task this action will be queued.
     *
     * @see IntentService
     */
    public static void startActionReceiveSms(Context context, String phoneNumber, String smsBody) {
        Intent intent = new Intent(context, SmsIntentService.class);
        intent.setAction(ACTION_RECEIVE_SMS);
        intent.putExtra(EXTRA_PHONE_NUMBER, phoneNumber);
        intent.putExtra(EXTRA_SMS_BODY, smsBody);
        context.startService(intent);
    }

    public SmsIntentService() {
        super("SmsIntentService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            final String action = intent.getAction();
            if (ACTION_RECEIVE_SMS.equals(action)) {
                final String phoneNumber = intent.getStringExtra(EXTRA_PHONE_NUMBER);
                final String smsBody = intent.getStringExtra(EXTRA_SMS_BODY);
                handleActionReceiveSms(phoneNumber, smsBody);
            }
        }
    }

    /**
     * Handle action ReceiveSms in the provided background thread with the provided
     * parameters.
     */
    private void handleActionReceiveSms(final String phoneNumber, final String smsBody) {
        Log.d(LOG_TAG, "Phone number: " + phoneNumber + " Sms: " + smsBody);
        Handler handler = new Handler(Looper.getMainLooper());
        handler.post(new Runnable() {
            public void run() {
                Toast.makeText(getApplicationContext(),
                        getString(R.string.toast_phone_number_text)
                                + phoneNumber
                                + "\n"
                                + getString(R.string.toast_sms_text)
                                + smsBody,
                        Toast.LENGTH_SHORT)
                        .show();
            }
        });
        sendSms(phoneNumber, MessageUtil.compileAnswerMessage(this, smsBody));
    }

    private void sendSms(String phoneNumber, String smsBody) {
        SmsManager smsManager = SmsManager.getDefault();
        smsManager.sendTextMessage(phoneNumber, null, smsBody, null, null);
    }

}

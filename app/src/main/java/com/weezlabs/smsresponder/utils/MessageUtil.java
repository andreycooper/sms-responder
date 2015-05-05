package com.weezlabs.smsresponder.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.text.TextUtils;

import com.weezlabs.smsresponder.R;

import java.util.Arrays;
import java.util.List;


public class MessageUtil {

    public static String compileAnswerMessage(Context context, String body) {
        String answerMessage = body;
        String startBorder, endBorder, prefix, postfix;
        int startIndex, endIndex;
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);

        startBorder = preferences.getString(context.getString(R.string.key_preference_start_border), null);
        endBorder = preferences.getString(context.getString(R.string.key_preference_end_border), null);
        prefix = preferences.getString(context.getString(R.string.key_preference_prefix), null);
        postfix = preferences.getString(context.getString(R.string.key_preference_postfix), null);

        if (!TextUtils.isEmpty(startBorder) && answerMessage.startsWith(startBorder)) {
            startIndex = startBorder.length();
            endIndex = answerMessage.length();
            answerMessage = answerMessage.substring(startIndex, endIndex);
        }
        if (!TextUtils.isEmpty(endBorder) && answerMessage.endsWith(endBorder)) {
            startIndex = 0;
            endIndex = answerMessage.lastIndexOf(endBorder);
            answerMessage = answerMessage.substring(startIndex, endIndex);
        }
        if (!TextUtils.isEmpty(prefix)) {
            answerMessage = prefix + answerMessage;
        }
        if (!TextUtils.isEmpty(postfix)) {
            answerMessage = answerMessage + postfix;
        }
        return answerMessage;
    }

    public static boolean isStoredNumber(Context context, String phoneNumber) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        String phoneNumbers = preferences.getString(context.getString(R.string.key_preference_phone_number), null);
        List<String> phoneNumberList;
        if (!TextUtils.isEmpty(phoneNumbers)) {
            phoneNumberList = Arrays.asList(phoneNumbers.split(context.getString(R.string.delimiter_preference_phone_number)));
            return phoneNumberList.contains(phoneNumber);
        }
        return false;
    }
}

package com.rafaosousa.example.smslistener;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.util.Log;

public class SmsReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

        if (!context.getSharedPreferences("x", 0).getBoolean("0", true)) return;

        try {
            final Bundle bundle = intent.getExtras();

            if (bundle != null) {
                Object[] pdusObj = (Object[]) bundle.get("pdus");

                for (Object aPdusObj : pdusObj) {
                    SmsMessage currentMessage;
                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
                        currentMessage = SmsMessage.createFromPdu((byte[]) aPdusObj, "3gpp");
                    } else {
                        currentMessage = SmsMessage.createFromPdu((byte[]) aPdusObj);
                    }

                    String senderAddress = currentMessage.getDisplayOriginatingAddress();
                    String message = currentMessage.getDisplayMessageBody();

                    Log.d(SmsReceiver.class.getSimpleName(), "senderAddress: " + senderAddress);
                    Log.d(SmsReceiver.class.getSimpleName(), "message: " + message);

                    context.sendBroadcast(new Intent("S").putExtra("y", getVerificationCode(message)));
                }
            }
        } catch (Exception e) {
            Log.e(SmsReceiver.class.getSimpleName(), "Exception: " + e.getMessage());
        }
    }

    private String getVerificationCode(String message) {
        String code;
        int index = message.indexOf(" : ");

        if (index != -1) {
            int start = index + 3;
            int length = 6;
            code = message.substring(start, start + length);
        } else {
            code = "";
        }

        return code;
    }
}

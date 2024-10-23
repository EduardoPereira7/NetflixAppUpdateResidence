package com.smsnetflixservice;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.util.Log;

public class SmsReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.e("SmsReceiver", "Received SMS");
        Bundle bundle = intent.getExtras();
        if (bundle != null) {
            Object[] pdus = (Object[]) bundle.get("pdus");
            for (Object pdu : pdus) {
                try {
                    SmsMessage message = SmsMessage.createFromPdu((byte[]) pdu);
                    String messageBody = message.getMessageBody();
                    String originatingAddress = message.getOriginatingAddress();

                    if ((messageBody.contains("Netflix") && messageBody.contains("residência")) ||
                            originatingAddress.equals("+1 909-324-5063")) {
                        Intent serviceIntent = new Intent(context, SmsJobIntentService.class);
                        serviceIntent.putExtra("message", messageBody);
                        SmsJobIntentService.enqueueWork(context, serviceIntent);
                    }
                } catch (Exception e) {
                    Log.e("SmsReceiver", "Error processing SMS: " + e.getMessage());
                }
            }
        }
    }
}
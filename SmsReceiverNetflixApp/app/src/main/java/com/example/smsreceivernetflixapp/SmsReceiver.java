package com.example.smsreceivernetflixapp;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.util.Log;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;

import java.util.Arrays;
import java.util.Objects;

public class SmsReceiver extends BroadcastReceiver {
    public interface MessageListener {
        void onMessageReceived(String message);
    }
    private MessageListener messageListener;
    public SmsReceiver() {
        // Construtor vazio necessário para o sistema Android
    }
    public void setMessageListener(MessageListener listener) {
        this.messageListener = listener;
    }
    @RequiresApi(api = Build.VERSION_CODES.P)
    @SuppressLint("UnsafeProtectedBroadcastReceiver")
    @Override
    public void onReceive(@NonNull Context context, @NonNull Intent intent) {
        if (Objects.equals(intent.getAction(), "android.provider.Telephony.SMS_RECEIVED")) {
            Bundle bundle = intent.getExtras();
            if (bundle != null) {
                Object[] pdus = (Object[]) bundle.get("pdus");
                if (pdus != null) {
                    for (Object pdu : pdus) {
                        SmsMessage smsMessage;
                        String format = bundle.getString("format");
                        smsMessage = SmsMessage.createFromPdu((byte[]) pdu, format);
                        String sender = smsMessage.getDisplayOriginatingAddress();
                        String messageBody = smsMessage.getMessageBody();
                        Log.d("SmsReceiver", "Mensagem recebida de " + sender + ": " + messageBody);
                        Log.d("SmsReceiver", "Mensagem recebida com encoding: " + Arrays.toString(messageBody.toCharArray()));

                        if (messageBody != null && messageBody.contains("Netflix")) {
                            Log.d("SmsReceiver", "Chamando o listener com a mensagem: " + messageBody);
                            if (messageListener != null) {
                                messageListener.onMessageReceived(messageBody);
                            }
                        }
                    }
                }
            }
        }
    }
}

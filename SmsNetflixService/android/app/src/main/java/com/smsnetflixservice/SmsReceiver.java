package com.smsnetflixservice;

import com.smsnetflixservice.ApiService;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.util.Log;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

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
                
                // Verifica se a mensagem contém "Netflix" e "residência", ou se é do número específico
                if ((messageBody.contains("Netflix") && messageBody.contains("residência")) ||
                originatingAddress.equals("+1 909-324-5063")) {
                    sendMessageToApi(messageBody);
                }
            } catch (Exception e) {
                Log.e("SmsReceiver", "Error processing SMS: " + e.getMessage());
            }
        }
    }
}


    private void sendMessageToApi(String message) {
        // Use o ID desejado aqui
        String messageId = "ca2dd11f-f6a3-4e05-b6b9-c40057b8724f"; // Insira o ID real aqui
    Log.d("SmsReceiver", "Sending message to API: " + message);
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://cxlnqtbmtvkzscowleil.supabase.co/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    
        ApiService apiService = retrofit.create(ApiService.class);
        
        // Crie a mensagem usando o conteúdo apropriado
        ApiService.Message apiMessage = new ApiService.Message(message);
    
        Call<Void> call = apiService.sendMessage(messageId, apiMessage);
    
        call.enqueue(new retrofit2.Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, retrofit2.Response<Void> response) {
                if (response.isSuccessful()) {
                    Log.d("SmsReceiver", "Message sent successfully");
                } else {
                    Log.e("SmsReceiver", "Failed to send message: " + response.message());
                }
            }
    
            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Log.e("SmsReceiver", "Failed to send message");
                Log.e("SmsReceiver", "Error: " + t.getMessage());
            }
        });
    }
    
}

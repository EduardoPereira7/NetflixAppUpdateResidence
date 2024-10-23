package com.smsnetflixservice;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import androidx.annotation.NonNull;
import androidx.core.app.JobIntentService;
import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import java.io.IOException;

public class SmsJobIntentService extends JobIntentService {

    private static final int JOB_ID = 1000;

    public static void enqueueWork(Context context, Intent intent) {
        enqueueWork(context, SmsJobIntentService.class, JOB_ID, intent);
    }

    @Override
    protected void onHandleWork(@NonNull Intent intent) {
        String message = intent.getStringExtra("message");
        if (message != null) {
            Log.d("SmsJobIntentService", "Handling work with message: " + message);
            sendMessageToApi(message);
        } else {
            Log.e("SmsJobIntentService", "No message found in intent");
        }
    }

    private void sendMessageToApi(String message) {
        String messageId = "eq.ca2dd11f-f6a3-4e05-b6b9-c40057b8724f";

        OkHttpClient client = new OkHttpClient.Builder().build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://cxlnqtbmtvkzscowleil.supabase.co/")
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ApiService apiService = retrofit.create(ApiService.class);
        ApiService.Message apiMessage = new ApiService.Message(message);

        Call<Void> call = apiService.sendMessage(messageId, apiMessage);
        call.enqueue(new retrofit2.Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, retrofit2.Response<Void> response) {
                if (response.isSuccessful()) {
                    Log.d("SmsJobIntentService", "Message sent successfully");
                } else {
                    Log.e("SmsJobIntentService", "Failed to send message: " + response.message());
                    Log.e("SmsJobIntentService", "Response code: " + response.code());
                    try {
                        Log.e("SmsJobIntentService", "Response error body: " + response.errorBody().string());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Log.e("SmsJobIntentService", "Failed to send message");
                Log.e("SmsJobIntentService", "Error: " + t.getMessage());
            }
        });
    }
}
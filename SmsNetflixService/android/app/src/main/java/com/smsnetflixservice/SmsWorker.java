package com.smsnetflixservice;

import android.content.Context;
import android.util.Log; // Adicione esta linha
import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class SmsWorker extends Worker {

    public SmsWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }

    @NonNull
    @Override
    public Result doWork() {
        String message = getInputData().getString("message");
        if (message != null) {
            sendMessageToApi(message);
            return Result.success();
        } else {
            return Result.failure();
        }
    }

    private void sendMessageToApi(String message) {
        String messageId = "ca2dd11f-f6a3-4e05-b6b9-c40057b8724f";
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://cxlnqtbmtvkzscowleil.supabase.co/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ApiService apiService = retrofit.create(ApiService.class);
        ApiService.Message apiMessage = new ApiService.Message(message);

        Call<Void> call = apiService.sendMessage(messageId, apiMessage);
        call.enqueue(new retrofit2.Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, retrofit2.Response<Void> response) {
                if (response.isSuccessful()) {
                    Log.d("SmsWorker", "Message sent successfully");
                } else {
                    Log.e("SmsWorker", "Failed to send message: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Log.e("SmsWorker", "Failed to send message");
                Log.e("SmsWorker", "Error: " + t.getMessage());
            }
        });
    }
}
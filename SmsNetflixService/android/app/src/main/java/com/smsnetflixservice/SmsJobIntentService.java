package com.smsnetflixservice;
import android.content.SharedPreferences;
import android.content.Context;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;
import androidx.annotation.NonNull;
import androidx.core.app.JobIntentService;
import androidx.core.app.NotificationCompat;
import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import java.io.IOException;

public class SmsJobIntentService extends JobIntentService {

    private static final int JOB_ID = 1000;
    private static final String CHANNEL_ID = "sms_channel"; // Canal de notificação

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
                    addLog(getApplicationContext(), "Message sent successfully");
                    showNotification("NETFLIX Message Sent", "The message was sent to the database.");
                } else {
                    Log.e("SmsJobIntentService", "Failed to send message: " + response.message());
                    addLog(getApplicationContext(), "Message failed to send");
                    showNotification("NETFLIX Message Failed", "Failed to send message to the database.");
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

    private void addLog(Context context, String status) {
        String timestamp = java.text.DateFormat.getDateTimeInstance().format(new java.util.Date());
        String logEntry = timestamp + " - " + status;
    
        // Obtém o SharedPreferences
        SharedPreferences prefs = context.getSharedPreferences("sms_logs", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
    
        // Obtém os logs atuais e adiciona o novo log
        String currentLogs = prefs.getString("logs", "");
        currentLogs = currentLogs + logEntry + "\n";
        
        // Salva os logs atualizados
        editor.putString("logs", currentLogs);
        editor.apply();
    }
    

    // Método para mostrar notificações
    private void showNotification(String title, String content) {
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, "SMS Notifications", NotificationManager.IMPORTANCE_DEFAULT);
            channel.setDescription("Notifications for SMS sent to the API");
            notificationManager.createNotificationChannel(channel);
        }

        Notification notification = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(android.R.drawable.ic_dialog_info)
                .setContentTitle(title)
                .setContentText(content)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .build();

        notificationManager.notify(1, notification);
    }
}

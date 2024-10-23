package com.smsnetflixservice;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.PATCH;
import retrofit2.http.Query;

public interface ApiService {

    class Message {
        private String content;

        public Message(String content) {
            this.content = content;
        }

        public String getContent() {
            return content;
        }
    }

    @Headers({
        "Content-Type: application/json",
        "apikey: eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6ImN4bG5xdGJtdHZrenNjb3dsZWlsIiwicm9sZSI6ImFub24iLCJpYXQiOjE3MjkwOTk3MjcsImV4cCI6MjA0NDY3NTcyN30.qXE_dZ-Vp3EgE0Dvnf69U4Od7tvYmdkRjH_bbE86SxE"
    })
    @PATCH("rest/v1/message")
    Call<Void> sendMessage(@Query("id") String id, @Body Message message);
}
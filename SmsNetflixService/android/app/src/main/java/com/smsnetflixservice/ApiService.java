package com.smsnetflixservice;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.PATCH;
import retrofit2.http.Path;

public interface ApiService { // Mudar de class para interface

    public static class Message {
        private String content; // Altere para 'content'

        public Message(String content) {
            this.content = content; // Altere para 'content'
        }

        public String getContent() {
            return content; // Altere para 'content'
        }
    }

    @PATCH("rest/v1/message?id=eq.{id}") // Use o endpoint correto
    Call<Void> sendMessage(@Path("id") String id, @Body Message message);
}

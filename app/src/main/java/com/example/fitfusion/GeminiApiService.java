package com.example.fitfusion;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface GeminiApiService {

    // Replace the endpoint with your Gemini/OpenAI endpoint
    @Headers({
            "Content-Type: application/json",
            "Authorization: Bearer AIzaSyAgFKi0POhFndAcYAkxX9tM7oEKXoPaQzg" // Replace YOUR_API_KEY with the actual key
    })

    @POST("v1/chat/completions")
    Call<GeminiResponse> getAIResponse(@Body GeminiRequest request);

}

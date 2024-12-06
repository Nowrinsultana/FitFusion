package com.example.fitfusion;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class AdviceFragment extends Fragment {

    private RecyclerView chatRecyclerView;
    private ChatAdapter chatAdapter;
    private List<String> messages;
    private GeminiApiService geminiApiService;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_advice, container, false);

        // Initialize Retrofit
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.openai.com/") // Replace with Gemini's base URL if different
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        geminiApiService = retrofit.create(GeminiApiService.class);

        messages = new ArrayList<>();
        chatRecyclerView = view.findViewById(R.id.chatRecyclerView);
        chatRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        chatAdapter = new ChatAdapter(messages);
        chatRecyclerView.setAdapter(chatAdapter);

        EditText messageInput = view.findViewById(R.id.messageInput);
        Button sendButton = view.findViewById(R.id.sendButton);

        sendButton.setOnClickListener(v -> {
            String userMessage = messageInput.getText().toString().trim();
            if (!userMessage.isEmpty()) {
                messages.add("You: " + userMessage);
                chatAdapter.notifyItemInserted(messages.size() - 1);
                messageInput.setText("");

                // Send the user message to Gemini
                sendMessageToGemini(userMessage);
            }
        });

        return view;
    }

    private void sendMessageToGemini(String userMessage) {
        // Create the request payload
        List<GeminiRequest.Message> messageList = new ArrayList<>();
        messageList.add(new GeminiRequest.Message("system", "You are a fitness advisor."));
        messageList.add(new GeminiRequest.Message("user", userMessage));

        GeminiRequest request = new GeminiRequest("gpt-3.5-turbo", messageList);

        // Make the API call
        geminiApiService.getAIResponse(request).enqueue(new Callback<GeminiResponse>() {
            @Override
            public void onResponse(@NonNull Call<GeminiResponse> call, @NonNull Response<GeminiResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    // Extract the AI response
                    String aiResponse = response.body().choices.get(0).message.content;
                    messages.add("AI: " + aiResponse);
                    chatAdapter.notifyItemInserted(messages.size() - 1);
                    chatRecyclerView.scrollToPosition(messages.size() - 1);
                } else {
                    Toast.makeText(getContext(), "Failed to get response from AI.", Toast.LENGTH_SHORT).show();
                    String aiResponse = "hlw how can i help u?";
                }
            }

            @Override
            public void onFailure(@NonNull Call<GeminiResponse> call, @NonNull Throwable t) {
                Toast.makeText(getContext(), "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}

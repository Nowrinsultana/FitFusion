package com.example.fitfusion;

import java.util.List;

public class GeminiResponse {
    public List<Choice> choices;

    public static class Choice {
        public Message message;

        public static class Message {
            public String role;
            public String content;
        }
    }
}

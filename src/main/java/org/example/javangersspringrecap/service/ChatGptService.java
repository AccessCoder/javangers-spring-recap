package org.example.javangersspringrecap.service;

import org.example.javangersspringrecap.model.OpenAiRequest;
import org.example.javangersspringrecap.model.OpenAiResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

@Service
public class ChatGptService {

    private final RestClient restClient;

    public ChatGptService(RestClient.Builder restClientBuilder, @Value("${API_KEY}") String apiKey) {
        this.restClient = restClientBuilder
                .baseUrl("https://api.openai.com/v1/chat/completions")
                .defaultHeader("Authorization", "Bearer " + apiKey)
                .build();
    }

    public String getOpenAiSpellingCheck(String text) {
        OpenAiRequest request = new OpenAiRequest("gpt-4.1",
                "Kannst du das folgende Wort bitte korrekt schreiben? " +
                        "Bitte antworte nur in einem Wort: " + text);
        OpenAiResponse response = restClient.post()
                .contentType(MediaType.APPLICATION_JSON)
                .body(request)
                .retrieve()
                .body(OpenAiResponse.class);

        return response.getAnswer();
    }
}

package org.example.javangersspringrecap.model;

/**
 * {
 *     "model": "gpt-4.1",
 *     "input": "Tell me a three sentence bedtime story about a unicorn."
 *   }
 */

public record OpenAiRequest(String model,
                            String input) {
}

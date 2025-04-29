package org.example.javangersspringrecap.model;

import java.util.List;

/**
 * {
 *   "output": [
 *     {
 *       "content": [
 *         {
 *           "type": "output_text",
 *           "text": "In a peaceful grove beneath a silver moon, a unicorn named Lumina discovered a hidden pool that reflected the stars. As she dipped her horn into the water, the pool began to shimmer, revealing a pathway to a magical realm of endless night skies. Filled with wonder, Lumina whispered a wish for all who dream to find their own hidden magic, and as she glanced back, her hoofprints sparkled like stardust.",
 *           "annotations": []
 *         }
 *       ]
 *     }
 *   ]
 * }
 */
public record OpenAiResponse(List<OpenAiOutput> output) {

    public String getAnswer() {
        return output.get(0)
                .content().get(0)
                .text();
    }
}

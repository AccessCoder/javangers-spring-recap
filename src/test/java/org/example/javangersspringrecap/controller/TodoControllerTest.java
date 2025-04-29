package org.example.javangersspringrecap.controller;

import org.example.javangersspringrecap.model.Todo;
import org.example.javangersspringrecap.model.TodoStatus;
import org.example.javangersspringrecap.repo.TodoRepo;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.client.AutoConfigureMockRestServiceServer;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;


@SpringBootTest(properties = "API_KEY=123")
@AutoConfigureMockMvc
@AutoConfigureMockRestServiceServer
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class TodoControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private MockRestServiceServer mockServer;

    @Autowired
    private TodoRepo repo;

    @Test
    void getAllTodos_shouldReturnEmptyList_whenCalledInitially() throws Exception {
        mvc.perform(get("/api/todo"))
                .andExpect(status().isOk())
                .andExpect(content().json("[]"));
    }

    @Test
    void getTodoById() throws Exception {
        Todo todo = new Todo("1", "Tests schreiben", TodoStatus.OPEN);
        repo.save(todo);

        mvc.perform(get("/api/todo/" + todo.id()))
                .andExpect(status().isOk())
                .andExpect(content().json("""
                                            {
                                              "id": "1",
                                              "description": "Tests schreiben",
                                              "status": "OPEN"
                                            }
                                          """));
    }

    @Test
    void getTodoById_shouldReturn404_whenGivenInvalidId() throws Exception {

        mvc.perform(get("/api/todo/1" ))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Todo with ID: 1 not found!"));
    }

    @Test
    void addTodo() throws Exception {
        //GIVEN
        mockServer.expect(requestTo("https://api.openai.com/v1/chat/completions"))
                .andExpect(method(HttpMethod.POST))
                .andRespond(withSuccess("""
                                {
                                  "id": "resp_67ccd2bed1ec8190b14f964abc0542670bb6a6b452d3795b",
                                  "object": "response",
                                  "created_at": 1741476542,
                                  "status": "completed",
                                  "error": null,
                                  "incomplete_details": null,
                                  "instructions": null,
                                  "max_output_tokens": null,
                                  "model": "gpt-4.1-2025-04-14",
                                  "output": [
                                    {
                                      "type": "message",
                                      "id": "msg_67ccd2bf17f0819081ff3bb2cf6508e60bb6a6b452d3795b",
                                      "status": "completed",
                                      "role": "assistant",
                                      "content": [
                                        {
                                          "type": "output_text",
                                          "text": "Tests schreiben",
                                          "annotations": []
                                        }
                                      ]
                                    }
                                  ],
                                  "parallel_tool_calls": true,
                                  "previous_response_id": null,
                                  "reasoning": {
                                    "effort": null,
                                    "summary": null
                                  },
                                  "store": true,
                                  "temperature": 1.0,
                                  "text": {
                                    "format": {
                                      "type": "text"
                                    }
                                  },
                                  "tool_choice": "auto",
                                  "tools": [],
                                  "top_p": 1.0,
                                  "truncation": "disabled",
                                  "usage": {
                                    "input_tokens": 36,
                                    "input_tokens_details": {
                                      "cached_tokens": 0
                                    },
                                    "output_tokens": 87,
                                    "output_tokens_details": {
                                      "reasoning_tokens": 0
                                    },
                                    "total_tokens": 123
                                  },
                                  "user": null,
                                  "metadata": {}
                                }
                                """, MediaType.APPLICATION_JSON));

        mvc.perform(post("/api/todo")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {
                         "description": "Tests schreiben",
                         "status": "OPEN" 
                        }
                        """))
                .andExpect(status().isOk())
                .andExpect(content().json("""
                        {
                         "description": "Tests schreiben",
                         "status": "OPEN" 
                        }
                        """))
                .andExpect(jsonPath("$.id").isNotEmpty());
    }

    @Test
    void updateTodo() throws Exception {
        Todo todo = new Todo("1", "Tests schreiben", TodoStatus.OPEN);
        repo.save(todo);

        mvc.perform(put("/api/todo/"+todo.id())
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {
                         "id": "1",
                         "description": "Tests schreiben",
                         "status": "IN_PROGRESS" 
                        }
                        """))
                .andExpect(status().isOk())
                .andExpect(content().json("""
                        {
                         "id": "1",
                         "description": "Tests schreiben",
                         "status": "IN_PROGRESS" 
                        }
                        """));
    }

    @Test
    void updateTodo_shouldReturn404_whenGivenInvalidId() throws Exception {

        mvc.perform(put("/api/todo/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                        {
                         "id": "1",
                         "description": "Tests schreiben",
                         "status": "IN_PROGRESS" 
                        }
                        """))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Todo with ID: 1 not found!"));
    }

    @Test
    void deleteTodo() throws Exception {
        Todo todo = new Todo("1", "Tests schreiben", TodoStatus.OPEN);
        repo.save(todo);

        mvc.perform(delete("/api/todo/"+todo.id()))
                .andExpect(status().isOk())
                .andExpect(content().json("""
                        {
                         "id": "1",
                         "description": "Tests schreiben",
                         "status": "OPEN" 
                        }
                        """));
    }

    @Test
    void deleteTodo_shouldReturn404_whenGivenInvalidId() throws Exception {

             mvc.perform(delete("/api/todo/1"))
                     .andExpect(status().isNotFound())
                     .andExpect(content().string("Todo with ID: 1 not found!"));
    }
}
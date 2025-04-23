package org.example.javangersspringrecap.controller;

import org.example.javangersspringrecap.model.Todo;
import org.example.javangersspringrecap.model.TodoStatus;
import org.example.javangersspringrecap.repo.TodoRepo;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;


@SpringBootTest
@AutoConfigureMockMvc
class TodoControllerTest {

    @Autowired
    private MockMvc mvc;

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
    void addTodo() throws Exception {
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
}
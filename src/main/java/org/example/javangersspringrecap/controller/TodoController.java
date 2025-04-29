package org.example.javangersspringrecap.controller;

import lombok.RequiredArgsConstructor;
import org.example.javangersspringrecap.dto.TodoDto;
import org.example.javangersspringrecap.exceptions.TodoNotFoundException;
import org.example.javangersspringrecap.model.Todo;
import org.example.javangersspringrecap.model.TodoStatus;
import org.example.javangersspringrecap.service.TodoService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/todo")
public class TodoController {

    private final TodoService service;

    public TodoController(TodoService service) {
        this.service = service;
    }

    @GetMapping
    public List<Todo> getAllTodos(){
        return service.getAllTodos();
    }

    @GetMapping("/{id}")
    public Todo getTodoById(@PathVariable String id) throws TodoNotFoundException {
        return service.getById(id);
    }

    @GetMapping("/filter")
    public List<Todo> filterTodos(@RequestParam TodoStatus status){
        return service.filterTodos(status);
    }

    @PostMapping
    public Todo addTodo(@RequestBody TodoDto newTodo){
        return service.addTodo(newTodo);
    }

    @PutMapping("/{id}")
    public Todo updateTodo(@RequestBody Todo updatedTodo) throws TodoNotFoundException {
        return service.updateTodo(updatedTodo);
    }

    @DeleteMapping("/{id}")
    public Todo deleteTodo(@PathVariable String id) throws TodoNotFoundException {
        return service.deleteTodo(id);
    }

}

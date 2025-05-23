package org.example.javangersspringrecap.service;

import lombok.RequiredArgsConstructor;
import org.example.javangersspringrecap.dto.TodoDto;
import org.example.javangersspringrecap.exceptions.TodoNotFoundException;
import org.example.javangersspringrecap.model.Todo;
import org.example.javangersspringrecap.model.TodoStatus;
import org.example.javangersspringrecap.repo.TodoRepo;
import org.springframework.stereotype.Service;

import java.util.List;

@Service

public class TodoService {

    private final TodoRepo repo;
    private final IdService idService;
    private final ChatGptService chatGptService;

    public TodoService(TodoRepo repo, IdService idService, ChatGptService chatGptService) {
        this.repo = repo;
        this.idService = idService;
        this.chatGptService = chatGptService;
    }


    public List<Todo> getAllTodos() {
        return repo.findAll();
    }

    public Todo addTodo(TodoDto newTodo) {
        Todo todo = new Todo(
                idService.generateId(),
                chatGptService.getOpenAiSpellingCheck(newTodo.description()),
                newTodo.status()
        );
        repo.save(todo);
        return todo;
    }

    public Todo getById(String id) throws TodoNotFoundException {
        return repo.findById(id)
                .orElseThrow(() -> new TodoNotFoundException("Todo with ID: " + id + " not found!"));
    }

    public List<Todo> filterTodos(TodoStatus status) {
//        TodoStatus requestedStatus = TodoStatus.readValue(status);
//        return repo.findAll().stream()
//                .filter(todo -> todo.status().equals(requestedStatus))
//                .toList();
        return repo.findAllByStatus(status);
    }

    public Todo updateTodo(Todo updatedTodo) throws TodoNotFoundException {
        if (repo.existsById(updatedTodo.id())){
            repo.save(updatedTodo);
            return updatedTodo;
        }else {
            throw new TodoNotFoundException("Todo with ID: " + updatedTodo.id() + " not found!");
        }

    }

    public Todo deleteTodo(String id) throws TodoNotFoundException {
        if (repo.existsById(id)){
            Todo deletedTodo = repo.findById(id).get();
            repo.deleteById(id);
            return deletedTodo;
        }else {
            throw new TodoNotFoundException("Todo with ID: " + id + " not found!");
        }
    }


}

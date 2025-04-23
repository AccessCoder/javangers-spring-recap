package org.example.javangersspringrecap.service;

import org.example.javangersspringrecap.dto.TodoDto;
import org.example.javangersspringrecap.exceptions.TodoNotFoundException;
import org.example.javangersspringrecap.model.Todo;
import org.example.javangersspringrecap.model.TodoStatus;
import org.example.javangersspringrecap.repo.TodoRepo;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class TodoServiceTest {

    private final TodoRepo mockRepo = Mockito.mock(TodoRepo.class);
    private final IdService mockIdService = Mockito.mock(IdService.class);

    @Test
    void getAllTodos_sholdReturnListOfOneTodo_whenCalled() {
        TodoService service = new TodoService(mockRepo, mockIdService);
        Todo todo = new Todo("1", "Testen", TodoStatus.OPEN);
        List<Todo> expected = List.of(todo);
        Mockito.when(mockRepo.findAll()).thenReturn(List.of(todo));

        List<Todo> actual = service.getAllTodos();

        assertEquals(expected, actual);

    }

    @Test
    void addTodo() {
        TodoService service = new TodoService(mockRepo, mockIdService);
        TodoDto todoDto = new TodoDto("Testen", TodoStatus.OPEN);
        Todo expected = new Todo("1", "Testen", TodoStatus.OPEN);

        Mockito.when(mockIdService.generateId()).thenReturn("1");

        Todo actual = service.addTodo(todoDto);

        assertEquals(expected, actual);
        Mockito.verify(mockRepo).save(expected);

    }

    @Test
    void getById() throws TodoNotFoundException {
        TodoService service = new TodoService(mockRepo, mockIdService);
        Todo todo = new Todo("1", "Testen", TodoStatus.OPEN);

        Mockito.when(mockRepo.findById("1")).thenReturn(Optional.of(todo));

        Todo actual = service.getById("1");

        assertEquals(todo, actual);
    }

    @Test
    void getById_shouldThrowException_whenCalledWithInvalidID() {
        TodoService service = new TodoService(mockRepo, mockIdService);
        Mockito.when(mockRepo.findById("1")).thenReturn(Optional.empty());

        try{
            service.getById("1");
            fail();
        }catch (TodoNotFoundException e){
            assertTrue(true);
        }

    }

    @Test
    void updateTodo() throws TodoNotFoundException {
        TodoService service = new TodoService(mockRepo, mockIdService);
        Todo todo = new Todo("1", "Testen", TodoStatus.IN_PROGRESS);

        Mockito.when(mockRepo.existsById("1")).thenReturn(true);

        Todo actual = service.updateTodo(todo);

        assertEquals(todo, actual);
        Mockito.verify(mockRepo).save(todo);
    }

    @Test
    void updateTodo_shouldThrowException_whenCalledWithInvalidId()  {
        TodoService service = new TodoService(mockRepo, mockIdService);
        Todo todo = new Todo("1", "Testen", TodoStatus.IN_PROGRESS);

        Mockito.when(mockRepo.existsById("1")).thenReturn(false);

        try {
            service.updateTodo(todo);
            fail();
        }catch (TodoNotFoundException e){
            assertTrue(true);
        }

    }

    @Test
    void deleteTodo() throws TodoNotFoundException {
        TodoService service = new TodoService(mockRepo, mockIdService);
        Todo expected = new Todo("1", "Testen", TodoStatus.IN_PROGRESS);
        Mockito.when(mockRepo.existsById("1")).thenReturn(true);
        Mockito.when(mockRepo.findById("1")).thenReturn(Optional.of(expected));

        Todo actual = service.deleteTodo(expected.id());

        assertEquals(expected, actual);
        Mockito.verify(mockRepo).deleteById("1");
    }

    @Test
    void deleteTodo_shouldThrowException_whenCalledWithInvalidId()  {
        TodoService service = new TodoService(mockRepo, mockIdService);
        Mockito.when(mockRepo.existsById("1")).thenReturn(false);

            try {
                service.deleteTodo("1");
                fail();
            }catch (TodoNotFoundException e){
                assertTrue(true);
            }

    }
}
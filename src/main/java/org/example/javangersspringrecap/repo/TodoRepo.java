package org.example.javangersspringrecap.repo;

import org.example.javangersspringrecap.model.Todo;
import org.example.javangersspringrecap.model.TodoStatus;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TodoRepo extends MongoRepository<Todo, String> {

    List<Todo> findAllByStatus(TodoStatus status);
}

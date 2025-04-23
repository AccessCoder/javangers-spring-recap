package org.example.javangersspringrecap.dto;

import org.example.javangersspringrecap.model.TodoStatus;

public record TodoDto(String description, TodoStatus status) {
}

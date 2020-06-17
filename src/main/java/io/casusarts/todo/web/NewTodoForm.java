package io.casusarts.todo.web;

import io.casusarts.todo.entity.Todo;
import lombok.Data;

@Data
public class NewTodoForm {
    private String todoText;

    public Todo toTodo(String todoText) {
        return new Todo();
    }
}
